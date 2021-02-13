package jp.ac.asojuku.azcafe.service.grading;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.config.MessageProperty;
import jp.ac.asojuku.azcafe.dto.GradingResultDto;
import jp.ac.asojuku.azcafe.dto.GradingTestCaseResultDto;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.util.FileUtils;

public class GradingJavaScript extends GradingProcess {
	private final String EXEC_SHELL = AZCafeConfig.getInstance().getGradingjavascript();
	private final String SRC_FNAME = "azcafe.js";
	private final int CHECKSTYLE_FULLSCORE = 50;	//チェックスタイルの満点（原点方式）
	
	public GradingJavaScript(Language lang) {
		super(lang);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public GradingResultDto execBatch(AssignmentTblEntity entity, String batchDir, String workDir,
			List<File> srcFileList) throws AZCafeException {
		return null;
	}

	@Override
	public GradingResultDto execBatch(AssignmentTblEntity entity, String batchDir, String workDir, String code)
			throws AZCafeException {
		GradingResultDto result = new GradingResultDto();

		try {
			//ソースファイル出力
			Path srcFile = getSrcFile(workDir,code);
			//////////////////////////////////////////
			// 1.実行
			//////////////////////////////////////////
			String jsFileName = srcFile.getFileName().toString();
			//テストケース分ループする
			for( TestCaseTblEntity testCase : entity.getTestCaseTblSet()) {
				if( execProgram(batchDir,workDir,jsFileName,testCase) ) {
					//実行成功したので結果を比較する
					GradingTestCaseResultDto testcaseResult = compereOutput(testCase,workDir) ;
					//結果をDTOに登録する
					result.addGradingTestCaseResult(testcaseResult);
				}
			}
			//点数チェック
			checkOutputScore(result);
			//jsはソース検査をしないので固定の結果を入れる
			result.setScoreForSource(CHECKSTYLE_FULLSCORE);
			String checkStyleMsg =  MessageProperty.getInstance().getProperty(MessageProperty.GRADING_CHECKSTYLE_JS);
			result.setCheckStyleMsg(checkStyleMsg);
			
		}catch(IOException e) {
			
		}catch(InterruptedException e) {
			
		}
		
		return result;
	}

	/**
	 * ソースファイル出力
	 * 
	 * @param workDir
	 * @param code
	 * @return
	 * @throws AZCafeException
	 */
	private Path getSrcFile(String workDir,String code) throws AZCafeException {
		//プログラムをファイルに出力する
		return FileUtils.outputFile(workDir, SRC_FNAME, code);
	}
	
	/**
	 * プログラムを実行する
	 * 
	 * @param batchDir
	 * @param workDir
	 * @param jsFName
	 * @param testCase
	 * @return
	 * @throws AZCafeException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private boolean execProgram(String batchDir,String workDir,String jsFName,TestCaseTblEntity testCase) throws AZCafeException, IOException, InterruptedException {
		boolean result = false;
		//実行用のバッチファイルパスを作成
		String execBatchFileName = batchDir + "/" + EXEC_SHELL;
		//入力情報をファイルに出力する
		Path inputPath = null;
		try {
			inputPath = FileUtils.outputFile(workDir, INPUT_TEXT, testCase.getInputText());
	
			ProcessBuilder pb = new ProcessBuilder(
					execBatchFileName,workDir,jsFName,OUTPUT_TEXT);
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

}
