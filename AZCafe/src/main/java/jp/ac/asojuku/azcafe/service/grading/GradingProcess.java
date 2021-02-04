package jp.ac.asojuku.azcafe.service.grading;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.util.FileUtils;

public abstract class GradingProcess {

	protected final String BATCH_FILE_NAME_PREFIX = "grading_";
	protected Language lang;
	
	public GradingProcess(Language lang) {
		this.lang = lang;
	}

	/**
	 * テキスト入力による採点処理実行
	 * @param userId
	 * @param code
	 * @throws AZCafeException
	 */
	public void execByText(Integer userId,String code) throws AZCafeException {

		Path srcFile = null;
		String workDir = null;
		try {
			//バッチディレクトリを取得
			String batchDir = AZCafeConfig.getInstance().getBatchdir();
			//バッチファイル名を取得
			String batchFName = batchDir + "/" + getBatchFileName();
			//作業ディレクトリを取得
			workDir = getWorkDir(userId);
			
			//プログラムをファイルに出力する
			String srcFileName = getCodeFileName(code);
			srcFile = FileUtils.outputFile(workDir, srcFileName, code);
			
			//バッチを実行する
			ProcessBuilder pb = new ProcessBuilder(batchFName,workDir,workDir);
			
			Process process;
			process = pb.start();

			//バッチ実行タイムアウトは１０秒
			int ret = process.waitFor();

			if( ret != 0){
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
				FileUtils.delete(workDir);
			}
		}
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
