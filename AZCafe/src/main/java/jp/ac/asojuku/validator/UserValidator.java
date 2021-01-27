package jp.ac.asojuku.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.err.ErrorCode;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.RoleId;


/**
 * ユーザー情報に関するバリデーションクラス
 * @author nishino
 *
 */
public class UserValidator extends Validator{


	/**
	 * ユーザー名のチェック
	 * 今のところ文字種は何でもOKとする
	 *
	 * @param name
	 * @param errors
	 * @throws AZCafeException
	 */
	public static void useName(String name,BindingResult err) throws AZCafeException{

		//必須
		if( StringUtils.isEmpty(name) ){
			setErrorcode("nickname",err,ErrorCode.ERR_MEMBER_ENTRY_NAME_ISNEED);
		}
		//最大文字数
		if( name != null && name.length() > 100){
			setErrorcode("nickname",err,ErrorCode.ERR_MEMBER_ENTRY_NAME);
		}
	}

	/**
	 * ニックネーム
	 *
	 * @param name
	 * @param errors
	 * @throws AZCafeException
	 */
	public static void useNickName(String nickname,BindingResult err) throws AZCafeException{

		//必須
		if( StringUtils.isEmpty(nickname) ){
			setErrorcode("nickname",err,ErrorCode.ERR_MEMBER_ENTRY_NICKNAME_ISNEED);
		}
		//最大文字数
		if( nickname != null && nickname.length() > 100){
			setErrorcode("nickname",err,ErrorCode.ERR_MEMBER_ENTRY_NICKNAME);
		}
	}
	/**
	 * メールアドレスのチェック
	 *
	 * @param mailAddress
	 * @param errors
	 * @throws AZCafeException
	 */
	public static void mailAddress(String mailAddress,BindingResult err) throws AZCafeException{

		//必須
		if( StringUtils.isEmpty(mailAddress) ){
			setErrorcode("mailadress",err,ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS_ISNEED);
		}
		if (mailAddress != null && !chkMailFormat(mailAddress)) {
			setErrorcode("mailadress",err,ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
		}
		//最大文字数
		if( mailAddress != null && mailAddress.length() > 256){
			setErrorcode("mailadress",err,ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
		}
	}

	/**
	 * コースＩＤのチェック
	 * @param couseId
	 * @param list
	 * @param errors
	 * @throws AZCafeException
	 */
	/*
	public static void courseId(String couseId,List<CourseDto> list,BindingResult err ) throws AZCafeException{

		int intCourseId;
		try{
			intCourseId = Integer.parseInt(couseId);

			boolean find = false;

			for(CourseDto dto : list ){
				if( dto.getCourseId()== intCourseId){
					find = true;
					break;
				}
			}

			if( !find ){
				setErrorcode("course_id",err,ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
			}

		}catch(NumberFormatException e){
			setErrorcode("course_id",err,ErrorCode.ERR_MEMBER_ENTRY_MAILADDRESS);
		}
	}
*/
	/**
	 * 入学年度
	 * @param admissionYear
	 * @param errors
	 * @throws AZCafeException
	 */
	public static void admissionYear(String admissionYear,BindingResult err) throws AZCafeException{

		try{
			//数値チェック
			Integer.parseInt(admissionYear);

			//4桁以下の場合は、西暦じゃないと判断しエラーとする
			if( admissionYear != null && admissionYear.length()  < 4){
				setErrorcode("admissionYear",err,ErrorCode.ERR_MEMBER_ENTRY_ADMISSIONYEAR_ERR);
			}

		}catch(NumberFormatException e){
			setErrorcode("admissionYear",err,ErrorCode.ERR_MEMBER_ENTRY_ADMISSIONYEAR);
		}
	}

	/**
	 * 卒業年度
	 * @param graduateYear
	 * @param errors
	 * @throws AZCafeException
	 */
	public static void graduateYear(String graduateYear,BindingResult err) throws AZCafeException{

		try{
			//数値チェック
			Integer.parseInt(graduateYear);

			//4桁以下の場合は、西暦じゃないと判断しエラーとする
			if( graduateYear != null && graduateYear.length()  < 4){
				setErrorcode("admissionYear",err,ErrorCode.ERR_MEMBER_ENTRY_GRADUATE_ERR);
			}

		}catch(NumberFormatException e){
			setErrorcode("admissionYear",err,ErrorCode.ERR_MEMBER_ENTRY_GRADUATEYEAR);
		}
	}

	/**
	 * 退学年度
	 * @param giveupYear
	 * @param errors
	 * @throws AZCafeException
	 */
	public static void gibeupYear(String giveupYear,BindingResult err) throws AZCafeException{

		try{
			//数値チェック
			Integer.parseInt(giveupYear);

			//4桁以下の場合は、西暦じゃないと判断しエラーとする
			if(giveupYear != null && giveupYear.length() < 4){
				setErrorcode("admissionYear",err,ErrorCode.ERR_MEMBER_ENTRY_GIVEUP_ERR);
			}

		}catch(NumberFormatException e){
			setErrorcode("admissionYear",err,ErrorCode.ERR_MEMBER_ENTRY_GIVEUPYEAR);
		}
	}

	/**
	 * ロールＩＤ
	 * @param roleId
	 * @param errors
	 * @throws AZCafeException
	 */
	public static void roleId(String roleId,BindingResult err) throws AZCafeException{

		int introleId;
		try{
			introleId = Integer.parseInt(roleId);

			if( !RoleId.check(introleId) ){
				setErrorcode("roleId",err,ErrorCode.ERR_MEMBER_ENTRY_ROLEID_ERR);
			}

		}catch(NumberFormatException e){
			setErrorcode("roleId",err,ErrorCode.ERR_MEMBER_ENTRY_ROLEID_ERR);
		}
	}

	/**
	 * パスワード
	 * @param password
	 * @param errors
	 * @throws AZCafeException
	 */
	public static void password(String password,BindingResult err) throws AZCafeException{
		String policy = AZCafeConfig.getInstance().getPasswordpolicy();

		if( !StringUtils.isEmpty(policy)){
			if(!password.matches(policy)){
				setErrorcode("pass1",err,ErrorCode.ERR_MEMBER_ENTRY_PASSWORD_POLICY);
			}

		}
	}
}
