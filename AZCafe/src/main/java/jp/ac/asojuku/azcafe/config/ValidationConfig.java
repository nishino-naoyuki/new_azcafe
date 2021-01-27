package jp.ac.asojuku.azcafe.config;

import jp.ac.asojuku.azcafe.err.ErrorCode;
import jp.ac.asojuku.azcafe.exception.AZCafeException;

/**
 * バリデーション用のメッセージクラス
 * @author nishino
 *
 */
public class ValidationConfig extends ConfigBase {

	public static final String ERR_PROP_PREFIX = "errmsg";

	public ValidationConfig() throws AZCafeException {
		super();
	}

	//シングルトン
	private static ValidationConfig prop = null;
	private static final String CONFIG_NAME = "ValidationMessages.properties";

	/**
	 * インスタンスの取得
	 * @return
	 * @throws BookStoreSystemErrorException
	 */
	public static ValidationConfig getInstance() throws AZCafeException{

		if( prop == null ){
			prop = new ValidationConfig();
		}

		return prop;
	}

	@Override
	protected String getConfigName() {
		return CONFIG_NAME;
	}

	/**
	 * エラーコードよりエラーメッセージを取得する
	 * @param code
	 * @return
	 */
	public String getMsg(ErrorCode code){

		return getProperty(ERR_PROP_PREFIX+code.getCode());
	}
}
