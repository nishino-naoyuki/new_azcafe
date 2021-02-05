package jp.ac.asojuku.azcafe.service.grading;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.util.FileUtils;

public abstract class GradingProcess {

	protected final String BATCH_FILE_NAME_PREFIX = "grading_";
	private final String INPUT_TEXT = "input.txt";
	protected Language lang;
	
	public GradingProcess(Language lang) {
		this.lang = lang;
	}
	
	public abstract ProcessBuilder execBatch(String batchFName,String workDir,String code) throws AZCafeException;

	/**
	 * テキスト入力による採点処理実行
	 * @param userId
	 * @param code
	 * @throws AZCafeException
	 */
	public void execByText(AssignmentTblEntity entity,Integer userId,String code) throws AZCafeException {

		Path srcFile = null;
		String workDir = null;
		try {
			//バッチディレクトリを取得
			String batchDir = AZCafeConfig.getInstance().getBatchdir();
			//バッチファイル名を取得
			String batchFName = batchDir + "/" + getBatchFileName();
			//作業ディレクトリを取得
			workDir = getWorkDir(userId);
			
			//テストケース分ループする
			for( TestCaseTblEntity testCase : entity.getTestCaseTblSet()) {
				if( execProcess(testCase,batchFName,workDir,code) ) {
					//実行成功したので結果を比較する
					boolean isSame = compereOutput(testCase,workDir) ;
					//結果をDTOに登録する
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new AZCafeException(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new AZCafeException(e);
		}finally {
			//一時ファイルの削除
			if( srcFile != null ) {
				//FileUtils.delete(workDir);
			}
		}
	}
	
	protected abstract boolean compereOutput(TestCaseTblEntity testCase,String workDir) throws AZCafeException;
	
	private boolean execProcess(
			TestCaseTblEntity testCase,
			String batchFName,
			String workDir,
			String code) throws AZCafeException, IOException, InterruptedException {
		
		Path inputPath = null;
		boolean result = false;
		try {
			//入力情報をファイルに出力する
			inputPath = FileUtils.outputFile(workDir, INPUT_TEXT, testCase.getInputText());
			//バッチを実行する
			ProcessBuilder pb = execBatch(batchFName,workDir,code);
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
	 * バッチファイルの名前を取得する
	 * @param lang
	 * @return
	 */
	public String getBatchFileName() {
		//バッチファイルの拡張子取得（WindowsとLinuxで違う）
		String ext = AZCafeConfig.getInstance().getBatchfileext();
		return BATCH_FILE_NAME_PREFIX + lang.getMsg() + "." + ext;
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
	 * ソースコードのファイルを名を取得する
	 * @param lang
	 * @param code
	 * @return
	 */
	public String getCodeFileName(String code) {
		
		String fileName = "";

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		fileName = AZCafeConfig.getInstance().getSrcPrefix() + sdf.format(now) + "." + lang.getExt();
		
		return fileName;
	}
}
