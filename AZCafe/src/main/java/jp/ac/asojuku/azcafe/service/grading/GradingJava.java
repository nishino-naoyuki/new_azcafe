package jp.ac.asojuku.azcafe.service.grading;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.thymeleaf.util.StringUtils;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.dto.GradingResultDto;
import jp.ac.asojuku.azcafe.dto.GradingTestCaseResultDto;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.util.FileUtils;


public class GradingJava extends GradingProcess {
	//クラス情報保持用の内部クラス
	private class ClassInfo{
		private boolean isMainClass;
		private String className;
		private String packageName;
		
		public ClassInfo(boolean isMainClass,String packageName,String className) 
		{this.isMainClass = isMainClass; this.className=className; this.packageName=packageName;}
		public boolean isMainClass() {return isMainClass;}
		public String getClassName() { return className; }
		public String getPackageName() { return packageName; }
	}
	private final String COMPLE_SHELL = AZCafeConfig.getInstance().getGradingjavac();
	private final String EXEC_SHELL = AZCafeConfig.getInstance().getGradingjava();
	private final String CHECKSTYLE_SHELL = AZCafeConfig.getInstance().getCheckstyle();
	private final String COMPLE_RESULT = "comple_result.txt";
	private final String CHECKSTYLE_RESULT = "checkstyle_result.txt";
	private final int CHECKSTYLE_FULLSCORE = 50;	//チェックスタイルの満点（原点方式）

	public GradingJava(Language lang) {
		super(lang);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/**
	 * ファイルリストの実行
	 */
	public GradingResultDto execBatch(AssignmentTblEntity entity,String batchDir,String workDir,List<File> srcFileList) throws AZCafeException {
		GradingResultDto result = new GradingResultDto();

		try {
			//////////////////////////////////////////
			// 1.ソースファイルに細工をする
			//////////////////////////////////////////
			List<ClassInfo> classInfoList = new ArrayList<>();
			for(File srcFile : srcFileList) {
				classInfoList.add( editSrcFile(workDir,srcFile) );
			}
			
			//////////////////////////////////////////
			// 2.コンパイル
			//////////////////////////////////////////
			if( comple(batchDir,workDir,result) != true ) {
				//コンパイル失敗
				return result;
			}
			//////////////////////////////////////////
			// 3.実行
			//////////////////////////////////////////
			//メインクラスを探す
			String className = getMainFullClassName(classInfoList);
			//テストケース分ループする
			for( TestCaseTblEntity testCase : entity.getTestCaseTblSet()) {
				if( execProgram(batchDir,workDir,className,testCase) ) {
					//実行成功したので結果を比較する
					GradingTestCaseResultDto testcaseResult = compereOutput(testCase,workDir) ;
					//結果をDTOに登録する
					result.addGradingTestCaseResult(testcaseResult);
				}
			}
			//点数チェック
			checkOutputScore(result);

			//////////////////////////////////////////
			// 3.ソース検査
			//////////////////////////////////////////
			checkStyle(batchDir,workDir,result);
			
		}catch(IOException e) {
			
		}catch(InterruptedException e) {
		}
		
		return result;
	}
	/**
	 *バッチ処理を開始する
	 *Javaの場合
	 *　コンパイル→実行（テストケースごと）→チェックスタイル
	 */
	public GradingResultDto execBatch(AssignmentTblEntity entity,String batchDir,String workDir,String code) throws AZCafeException {
		GradingResultDto result = new GradingResultDto();
		
		try {
			//ソースファイル出力
			Path srcFile = getSrcFile(workDir,code);
			//////////////////////////////////////////
			// 1.コンパイル
			//////////////////////////////////////////
			if( comple(batchDir,workDir,result) != true ) {
				//コンパイル失敗
				return result;
			}
			//////////////////////////////////////////
			// 2.実行
			//////////////////////////////////////////
			String className = FileUtils.getPreffix( FileUtils.getFileNameFromPath(srcFile.toString()) );
			//テストケース分ループする
			for( TestCaseTblEntity testCase : entity.getTestCaseTblSet()) {
				if( execProgram(batchDir,workDir,className,testCase) ) {
					//実行成功したので結果を比較する
					GradingTestCaseResultDto testcaseResult = compereOutput(testCase,workDir) ;
					//結果をDTOに登録する
					result.addGradingTestCaseResult(testcaseResult);
				}
			}
			//点数チェック
			checkOutputScore(result);

			//////////////////////////////////////////
			// 3.ソース検査
			//////////////////////////////////////////
			checkStyle(batchDir,workDir,result);
			
		}catch(IOException e) {
			
		}catch(InterruptedException e) {
			
		}
		
		return result;
	}
	
	/**
	 * ソースファイルを解析してパッケージ名やクラス名の情報を保持する
	 * 
	 * @param workDir
	 * @param srcFile
	 * @return
	 * @throws AZCafeException
	 * @throws IOException
	 */
	private ClassInfo editSrcFile(String workDir,File srcFile) throws AZCafeException, IOException {
		//一旦ファイルを1行筒読み込む
		List<String> codeList = FileUtils.readLine(srcFile.getAbsolutePath());
		
		StringBuilder sb = new StringBuilder();
		boolean isMainClass = false;
		String packagename = "";
		String className = "";
		//もし「package」があれば消す
		for( String line : codeList ) {
			if( isMainClass( line )) {
				//メインクラス
				isMainClass = true;
			}
			if( line.startsWith("package")) {
				//パッケージ名を取得する
				packagename = getPackageName(line);
			}
			sb.append(line);
		}
		
		//クラス名を取得する
		className = getClassName(sb.toString());
		
		//ファイルをコピーする
		FileUtils.copy(srcFile.getAbsolutePath(), workDir+"/"+srcFile.getName());
		
		return new ClassInfo(isMainClass,packagename,className);
	}
	
	/**
	 * パッケージ名を取得する
	 * 
	 * @param pgLine
	 * @return
	 */
	private String getPackageName(String pgLine) {
		String packageName = "";
		
		//空白を削除する
		pgLine = pgLine.replace(" ", "");
		pgLine = pgLine.replace("\t", "");
		
		//package宣言を見つける
		int index = pgLine.indexOf("package");
		if( index != -1 ) {
			packageName = pgLine.substring(
					index + "package".length(),
					( packageName.endsWith(";") ? pgLine.length()-2 : pgLine.length()-1)
					);
		}
		
		return packageName;
	}
	
	/**
	 * Mainメソッドを持つクラスかどうかを判定する
	 * 
	 * @param line
	 * @return
	 */
	private boolean isMainClass(String line) {
		
		return  line.contains("public") && 
				line.contains("static") && 
				line.contains("void") &&
				line.contains("main") &&
				line.contains("String[]");
		
	}
	
	/**
	 * メソッドを持つクラスの名前をパッケージ名付きで返す
	 * @param classInfoList
	 * @return
	 */
	private String getMainFullClassName(List<ClassInfo> classInfoList) {
		String fullClassName = "";
		for(ClassInfo classInfo : classInfoList) {
			if( classInfo.isMainClass() ) {
				if( StringUtils.isEmpty( classInfo.getPackageName()) ) {
					fullClassName = classInfo.getClassName();
				}else {
					fullClassName = classInfo.getPackageName() +"."+ classInfo.getClassName();
				}
				
				break;
			}
		}
		return fullClassName;
	}
	
	/**
	 * コンパイルの実行
	 * 
	 * @param batchDir
	 * @param workDir
	 * @param result
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private boolean comple(String batchDir,String workDir,GradingResultDto result) throws IOException, InterruptedException {
		boolean bCompleOK = false;

		//コンパイルに必要なフォルダ作成
		makeDir(workDir);
		
		ProcessBuilder pb = new ProcessBuilder(
				batchDir+"/"+COMPLE_SHELL,workDir,COMPLE_RESULT);
		//実行する
		Process process = pb.start();

		//バッチ実行タイムアウトは１０秒
		int ret = process.waitFor();

		if( ret == 0){
			//実行成功
			List<String> compleRsult = FileUtils.readLine(workDir+"/"+COMPLE_RESULT);
			bCompleOK = (compleRsult.size() == 0);
		}else {
			//コンパイル失敗
			getGradingResultWhenCompleError(workDir,result);
		}
		
		return bCompleOK;
	}
	
	/**
	 * コンパイル失敗時
	 * 
	 * @param workDir
	 * @param result
	 */
	private void getGradingResultWhenCompleError(String workDir,GradingResultDto result) {
		result.setCorrect(false);
		//コンパイルエラーメッセージを取得する
		List<String> compleRsult = FileUtils.readLine(workDir+"/"+COMPLE_RESULT);
		StringBuilder sb = new StringBuilder();
		for( String msg : compleRsult) {
			sb.append(msg).append("\n");
		}
		result.setCompleErrMsg(sb.toString());
	}
	
	/**
	 * プログラム実行
	 * 
	 * @param batchDir
	 * @param workDir
	 * @param className
	 * @param testCase
	 * @return
	 * @throws InterruptedException
	 * @throws AZCafeException
	 * @throws IOException
	 */
	private boolean execProgram(String batchDir,String workDir,String className,TestCaseTblEntity testCase) throws InterruptedException, AZCafeException, IOException {
		boolean result = false;
		//実行用のバッチファイルパスを作成
		String execBatchFileName = batchDir + "/" + EXEC_SHELL;
		//入力情報をファイルに出力する
		Path inputPath = null;
		try {
			inputPath = FileUtils.outputFile(workDir, INPUT_TEXT, testCase.getInputText());
	
			ProcessBuilder pb = new ProcessBuilder(
					execBatchFileName,workDir,className,OUTPUT_TEXT);
			//標準入力をファイルにする
			pb.redirectInput(inputPath.toFile());
			//実行する
			Process process = pb.start();
			//バッチ実行タイムアウトは１０秒
			int ret = process.waitFor();
	
			if( ret == 0){
				//実行成功
				result = true;
			}
		}finally {
			if( inputPath != null ) {
				FileUtils.delete(inputPath.toFile());
			}
		}
		return result;
	}
	
	
	/**
	 * チェックスタイル
	 * 
	 * @param batchDir
	 * @param workDir
	 * @param result
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void checkStyle(String batchDir,String workDir,GradingResultDto result) throws IOException, InterruptedException {
		
		ProcessBuilder pb = new ProcessBuilder(
				batchDir + "/" + CHECKSTYLE_SHELL,workDir,batchDir,CHECKSTYLE_RESULT);
		//実行する
		Process process = pb.start();
		//バッチ実行タイムアウトは１０秒
		int ret = process.waitFor();
		//戻り値はエラーの数
		if( ret == 0){
			//エラーがない
			result.setScoreForSource(CHECKSTYLE_FULLSCORE);
		}else {
			//エラーがある
			getCheckStyleMsg(workDir + "/" + CHECKSTYLE_RESULT,result);
		}
	}
	
	/**
	 * チェックスタイルの結果ファイルからメッセージを取得する
	 * チェックスタイルの結果ファイルは
	 * ---
	 * 監査を開始しています...
	 * [ERROR] C:\code\1\Test.java:5:24: メソッド名は先頭小文字で文節を大文字にしてください [MethodName]
	 * 監査が完了しました。
	 * ---
	 * のように1行目と最終行には固定の文字列が出力されるので
	 * 2行目から最終行ー１まで読む
	 * 
	 * @param checkStyleResultFile
	 * @return
	 */
	private void getCheckStyleMsg(String checkStyleResultFile,GradingResultDto result){
		List<String> list = FileUtils.readLine(checkStyleResultFile);
		int score = CHECKSTYLE_FULLSCORE;
		
		StringBuilder sb = new StringBuilder();
		for( int i = 1; i < list.size()-1; i++ ) {
			if( sb.length() > 0 ) {
				sb.append("\n");
			}
			sb.append(list.get(i));	
			score -= 5;	//一つの指摘当たり5点減点
		}
		
		if( score < 0) {
			score = 0;
		}
		result.setScoreForSource(score);
		result.setCheckStyleMsg(sb.toString());
	}
	
	/**
	 * ソースコードをファイル出力する
	 * 
	 * @param workDir
	 * @param code
	 * @return
	 * @throws AZCafeException
	 */
	private Path getSrcFile(String workDir,String code) throws AZCafeException {

		//プログラムをファイルに出力する
		String className = getClassName(code);
		String srcFileName = getCodeFileName(className);
		return FileUtils.outputFile(workDir, srcFileName, code);
	}
	
	public void makeDir(String workDir) {
		FileUtils.makeDir(workDir+"/classes");
	}
	/**
	 * ソースコードのファイルを名を取得する
	 * @param lang
	 * @param code
	 * @return
	 */
	public String getCodeFileName(String className) {
		
		String fileName = "";
		fileName = className+ ".java";
		
		return fileName;
	}
	
	/**
	 * ソースコードからクラス名を取得する
	 * @param code
	 * @return
	 */
	private String getClassName(String code) {
		
		//改行と{ } を削除
		code = code.replace("\n", "");
		code = code.replace("\r", "");
		code = code.replace("{", "");
		code = code.replace("}", "");
		
		String[] codeArry = code.split(" ");
		
		boolean findFlg = false;
		String ClassName = "";
		for( String word : codeArry) {
			if( word.equals("class")) {
				findFlg = true;
			}else if( findFlg && word.length() > 0) {
				//classの後の言葉
				ClassName = word;
				break;
			}
		}
		
		if( ClassName.length() == 0) {
			return AZCafeConfig.getInstance().getSrcPrefix();
		}
		
		return ClassName;
	}
	
}
