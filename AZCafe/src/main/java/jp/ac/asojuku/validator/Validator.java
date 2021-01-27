package jp.ac.asojuku.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.config.ValidationConfig;
import jp.ac.asojuku.azcafe.err.ErrorCode;
import jp.ac.asojuku.azcafe.exception.AZCafeException;

public abstract class Validator {
	protected static String mailFormat = "^[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+(\\.[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+)*+(.*)@[a-zA-Z0-9][a-zA-Z0-9\\-]*(\\.[a-zA-Z0-9\\-]+)+$";

	/**
	 * メールアドレスの形式チェック
	 * 
	 * @param mail
	 * @return
	 */
	protected static boolean chkMailFormat(String mail) {

		if (!mail.matches(mailFormat)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * エラーコードをセットする
	 * 
	 * @param columnName
	 * @param error
	 * @param errCode
	 * @throws AsoBbsSystemErrException
	 */
	public static void setErrorcode(String columnName,BindingResult error,ErrorCode errCode) throws AZCafeException {
		error.rejectValue(columnName,
				ValidationConfig.ERR_PROP_PREFIX+errCode.getCode(),
				ValidationConfig.getInstance().getMsg(errCode));
	}
}
