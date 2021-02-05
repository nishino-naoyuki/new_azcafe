package jp.ac.asojuku.azcafe.service.grading;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.dto.GradingResult;
import jp.ac.asojuku.azcafe.dto.GradingTestCaseResult;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.util.FileUtils;


public class GradingJava extends GradingProcess {
	private final String COMPLE_SHELL = "comple_java.bat";
	private final String EXEC_SHELL = "exec_java.bat";
	private final String CHECKSTYLE_SHELL = "checkstyle_java.bat";
	private final String COMPLE_RESULT = "comple_result.txt";
	private final String CORRECT_TEXT = "correct.txt";
	private final String OUTPUT_TEXT = "output.txt";
	private final String INPUT_TEXT = "input.txt";
	private final String CHECKSTYLE_RESULT = "checkstyle_result.txt";
	private final int CHECKSTYLE_FULLSCORE = 50;	//チェックスタイルの満点（原点方式）
	private final int OUTPUT_FULLSCORE = 50;	//出力結果の満点（50 or 0）

	public GradingJava(Language lang) {
		super(lang);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/**
	 *バッチ処理を開始する
	 *Javaの場合
	 *　コンパイル→実行（テストケースごと）→チェックスタイル
	 */
	public GradingResult execBatch(AssignmentTblEntity entity,String batchDir,String workDir,String code) throws AZCafeException {
		GradingResult result = new GradingResult();
		
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
					GradingTestCaseResult testcaseResult = compereOutput(testCase,workDir) ;
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
	 * コンパイルの実行
	 * 
	 * @param batchDir
	 * @param workDir
	 * @param result
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private boolean comple(String batchDir,String workDir,GradingResult result) throws IOException, InterruptedException {
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
	private void getGradingResultWhenCompleError(String workDir,GradingResult result) {
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
	 * 出力結果の点数
	 * 
	 * @param result
	 */
	private void checkOutputScore(GradingResult result) {
		boolean isAllOK = true;	//全ての出力結果がOK
		
		for( GradingTestCaseResult testcase : result.getTestCaseResultList() ) {
			if( testcase.isCorrect() != true ) {
				isAllOK = false;
				break;
			}
		}
		
		result.setCorrect(isAllOK);
		result.setScoreForOutput((isAllOK ? OUTPUT_FULLSCORE:0));
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
	private void checkStyle(String batchDir,String workDir,GradingResult result) throws IOException, InterruptedException {
		
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
	private void getCheckStyleMsg(String checkStyleResultFile,GradingResult result){
		List<String> list = FileUtils.readLine(checkStyleResultFile);
		int score = CHECKSTYLE_FULLSCORE;
		
		for( int i = 1; i < list.size()-1; i++ ) {
			result.addCheckStyleMsgList( list.get(i) );
			score -= 5;	//一つの指摘当たり5点減点
		}
		
		if( score < 0) {
			score = 0;
		}
		result.setScoreForSource(score);
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
	
	/**
	 * 結果を比較する
	 */
	protected GradingTestCaseResult compereOutput(TestCaseTblEntity testCase,String workDir) throws AZCafeException {

		GradingTestCaseResult testcaseResult = new GradingTestCaseResult();
		
		//正解ファイルを出力する
		Path correctPath = FileUtils.outputFile(workDir, CORRECT_TEXT, testCase.getOutputTxt());
		
		//出力ファイルと正解ファイルを比較する
		boolean isSame = 
				FileUtils.fileCompare(correctPath.toString(), workDir+"/"+OUTPUT_TEXT);
		
		//ユーザーの出力ファイルの文字列を取得する
		List<String> userOutputList = FileUtils.readLine(workDir+"/"+OUTPUT_TEXT);
		StringBuilder sb = new StringBuilder();;
		for(String line : userOutputList) {
			if( sb.length() > 0 ) {
				sb.append("\n");
			}
			sb.append(line);	
		}

		//DTO作成
		testcaseResult.setTestcaseId(testCase.getTestcaseId());
		testcaseResult.setCorrect(isSame);
		testcaseResult.setCorrectOutput(testCase.getOutputTxt());
		testcaseResult.setUserOutput(sb.toString());
		
		return testcaseResult;
		
	}
}
