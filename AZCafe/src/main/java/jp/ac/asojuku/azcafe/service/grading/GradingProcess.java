package jp.ac.asojuku.azcafe.service.grading;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.dto.GradingResultDto;
import jp.ac.asojuku.azcafe.dto.GradingTestCaseResultDto;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.util.FileUtils;

public abstract class GradingProcess {

	protected final String OUTPUT_TEXT = "output.txt";
	protected final String INPUT_TEXT = "input.txt";
	private final String CORRECT_TEXT = "correct.txt";
	private final int OUTPUT_FULLSCORE = 50;	//出力結果の満点（50 or 0）
	
	protected Language lang;
	
	public GradingProcess(Language lang) {
		this.lang = lang;
	}
	
	public abstract GradingResultDto execBatch(AssignmentTblEntity entity,String batchDir,String workDir,List<File> srcFileList) throws AZCafeException;
	public abstract GradingResultDto execBatch(AssignmentTblEntity entity,String batchDir,String workDir,String code) throws AZCafeException;

	/**
	 * テキスト入力による採点処理実行
	 * @param userId
	 * @param code
	 * @throws AZCafeException
	 */
	public GradingResultDto execByText(AssignmentTblEntity entity,Integer userId,String code) throws AZCafeException {

		GradingResultDto gradingResult = new GradingResultDto();
		String workDir = null;
		try {
			//バッチディレクトリを取得
			String batchDir = AZCafeConfig.getInstance().getBatchdir();
			//作業ディレクトリを取得
			workDir = getWorkDir(userId);
			
			gradingResult = execBatch(entity,batchDir,workDir,code);
			
		}finally {
			//一時ファイルの削除
			if( workDir != null ) {
//				FileUtils.delete(workDir);
			}
		}
		return gradingResult;
	}
	

	public GradingResultDto execByFile(AssignmentTblEntity entity,Integer userId,List<File> fileList) throws AZCafeException {

		GradingResultDto gradingResult = new GradingResultDto();
		String workDir = null;
		try {
			//バッチディレクトリを取得
			String batchDir = AZCafeConfig.getInstance().getBatchdir();
			//作業ディレクトリを取得
			workDir = getWorkDir(userId);
			
			gradingResult = execBatch(entity,batchDir,workDir,fileList);
			
		}finally {
			//一時ファイルの削除
			if( workDir != null ) {
//				FileUtils.delete(workDir);
			}
		}
		return gradingResult;
	}
	
	/**
	 * 個人用の作業ディレクトリを作成する
	 * 
	 * @param userId
	 * @return
	 */
	public String getWorkDir(Integer userId) {		
		return AZCafeConfig.getInstance().getCodedir() + "/" + userId;
	}

	/**
	 * 結果を比較する
	 */
	protected GradingTestCaseResultDto compereOutput(TestCaseTblEntity testCase,String workDir) throws AZCafeException {

		GradingTestCaseResultDto testcaseResult = new GradingTestCaseResultDto();
		
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
		testcaseResult.setInput(testCase.getInputText());
		testcaseResult.setCorrectOutput(testCase.getOutputTxt());
		testcaseResult.setUserOutput(sb.toString());
		
		return testcaseResult;
		
	}

	/**
	 * 出力結果の点数
	 * 
	 * @param result
	 */
	protected void checkOutputScore(GradingResultDto result) {
		boolean isAllOK = true;	//全ての出力結果がOK
		
		for( GradingTestCaseResultDto testcase : result.getTestCaseResultList() ) {
			if( testcase.isCorrect() != true ) {
				isAllOK = false;
				break;
			}
		}
		
		result.setCorrect(isAllOK);
		result.setScoreForOutput((isAllOK ? OUTPUT_FULLSCORE:0));
	}
}
