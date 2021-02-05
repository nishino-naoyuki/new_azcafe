package jp.ac.asojuku.azcafe.service.grading;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.dto.GradingResult;
import jp.ac.asojuku.azcafe.dto.GradingTestCaseResult;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.util.FileUtils;

public abstract class GradingProcess {

	protected Language lang;
	
	public GradingProcess(Language lang) {
		this.lang = lang;
	}
	
	public abstract GradingResult execBatch(AssignmentTblEntity entity,String batchDir,String workDir,String code) throws AZCafeException;
	protected abstract GradingTestCaseResult compereOutput(TestCaseTblEntity testCase,String workDir) throws AZCafeException;

	/**
	 * テキスト入力による採点処理実行
	 * @param userId
	 * @param code
	 * @throws AZCafeException
	 */
	public GradingResult execByText(AssignmentTblEntity entity,Integer userId,String code) throws AZCafeException {

		GradingResult gradingResult = new GradingResult();
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
				FileUtils.delete(workDir);
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
	
	
}
