/**
 *
 */
package jp.ac.asojuku.azcafe.err;

/**
 * エラーコード
 * @author nishino
 *
 */
public enum ErrorCode {

	SUCCESS("0000") ,
	//ログイン(00xx)
	ERR_LOGIN("0001"),
	//会員登録(01xx)
	ERR_MEMBER_ENTRY_MAILADDRESS("0101"),
	ERR_MEMBER_ENTRY_NAME("0102"),
	ERR_MEMBER_ENTRY_ADDRESS("0103"),
	ERR_MEMBER_ENTRY_TELNUMBER("0104"),
	ERR_MEMBER_ENTRY_MAILADDRESS_ISNEED("0105"),
	ERR_MEMBER_ENTRY_NAME_ISNEED("0106"),
	ERR_MEMBER_ENTRY_ADDRESS_ISNEED("0107"),
	ERR_MEMBER_ENTRY_TELNUMBER_ISNEED("0108"),
	ERR_MEMBER_ENTRY_PASSWORD_NOTMATCH("0109"),
	ERR_MEMBER_ENTRY_PASSWORD_POLICY("0110"),
	ERR_MEMBER_ENTRY_DUPLICATE_MEIL("0111"),
	ERR_MEMBER_ENTRY_NICKNAME("0113"),
	ERR_MEMBER_ENTRY_NICKNAME_ISNEED("0112"),
	ERR_MEMBER_ENTRY_COURSEID_ERR("0114"),
	ERR_MEMBER_ENTRY_ADMISSIONYEAR("0115"),
	ERR_MEMBER_ENTRY_ADMISSIONYEAR_ERR("0116"),
	ERR_MEMBER_ENTRY_GRADUATEYEAR("0117"),
	ERR_MEMBER_ENTRY_GRADUATE_ERR("0118"),
	ERR_MEMBER_ENTRY_GIVEUPYEAR("0119"),
	ERR_MEMBER_ENTRY_GIVEUP_ERR("0120"),
	ERR_MEMBER_ENTRY_ROLEID_ERR("0121"),
	ERR_MEMBER_ENTRY_DIFFICALTY_ERR("0122"),
	ERR_MEMBER_ENTRY_MAILNOTFOUND_ERR("0123"),
	ERR_MEMBER_ENTRY_DUPLICATE_STUDENTNO("0124"),
	//ルーム関連(02xx)
	ERR_ROOM_NAME_ISNEED("0201"),
	ERR_ROOM_NAME_ERROR("0202"),
	ERR_ROOM_ADMIN_ISNEED("0203"),
	ERR_ROOM_BELONG_ISNEED("0204"),
	ERR_ROOM_ADMIN_FMT_ERROR("0205"),
	ERR_ROOM_BELONG_FMT_ERROR("0206"),
	ERR_ROOM_MAIL_NOT_FOUND("0207"),
	ERR_ROOM_DUPLICATE_ROOMNAME("0208"),
	ERR_ROOM_ADMIN_MAIL_NOT_FOUND("0209"),
	ERR_ROOM_USER_MAIL_NOT_FOUND("0210"),
	
	//掲示板関連(03xx)
	ERR_CSV_FORMAT_ERROR("0401"),
	//お知らせ関連(04xx)
	ERR_INFO_TITLE_LEN("0401"),
	ERR_INFO_MSG_LEN("0402"),
	
	//パスワード関連
	ERR_PWD_CHG_NOT_MATCH("0501"),
	ERR_PWD_CHG_OLD_PWD_EMPTY("0502"),
	ERR_PWD_CHG_NEW_PWD_EMPTY("0503"),
	ERR_PWD_CHG_OLD_PWD_WRONG("0504"),
	ERR_PWD_CHG_MAIL_EMPTY("0505"),
	ERR_PWD_CHG_MAIL_NOTEXIST("0506"),

	//その他のエラー
	ERR_INVLIDATE("9901"),
	ERR_SYSTEM_ERROR("9999"),
	;

	private String code;

	private ErrorCode(String code){ this.code = code; }

	public String getCode(){ return code; }

}
