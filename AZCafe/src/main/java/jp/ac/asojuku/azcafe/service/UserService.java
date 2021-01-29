package jp.ac.asojuku.azcafe.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.dto.CreateUserDto;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.entity.AutologinTblEntity;
import jp.ac.asojuku.azcafe.entity.HomeroomTblEntity;
import jp.ac.asojuku.azcafe.entity.LevelTblEntity;
import jp.ac.asojuku.azcafe.entity.UserTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.RoleId;
import jp.ac.asojuku.azcafe.repository.AutoLoginRepository;
import jp.ac.asojuku.azcafe.repository.UserRepository;
import jp.ac.asojuku.azcafe.util.Digest;
import jp.ac.asojuku.azcafe.util.Token;

@Service
public class UserService {

	@Autowired 
	UserRepository userRepository;
	@Autowired
	AutoLoginRepository autoLoginRepository;
	@Autowired
	AZCafeConfig config;
	
	/**
	 * ユーザーIDを指定してユーザー情報を取得する
	 * ユーザー情報の再取得の時に使用する
	 * 
	 * @param userId
	 * @return
	 */
	public LoginInfoDto getLoginInfo(Integer userId) {
		UserTblEntity userTblEntity = userRepository.getOne(userId);
		
		return getFrom(userTblEntity);
	}
	/**
	 * 1件追加
	 * 
	 * @param createUserDto
	 * @throws AZCafeException
	 */
	public void insert(CreateUserDto createUserDto) throws AZCafeException {
		
		//DTO->Entity
		UserTblEntity entity = getFrom(createUserDto,true);
		
		userRepository.saveAndFlush(entity);
	}
	/**
	 * ログイン処理を行う
	 * @param mail
	 * @param password
	 * @return ログイン情報（ログイン失敗時はnull）
	 * @throws AZCafeException
	 */
	public LoginInfoDto login(String mail,String password) 
			throws AZCafeException{
		LoginInfoDto dto = null;
		
		//ハッシュ計算する
		String hashedPwd  = Digest.createPassword(mail, password);

		//ログインユーザー検索
		UserTblEntity entity = userRepository.getUser(mail, hashedPwd);
		
		if( entity!= null ) {
			//entity -> dto
			dto =getFrom(entity);
		}
		
		return dto;
	}

	/**
	 * トークンを発行してDBに保存する
	 * 
	 * @param userId
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	public String createLoginToken(Integer userId) throws AZCafeException {
		//トークンの発行
		String token = Token.getCsrfToken();
		//有効期限の設定
		int addDays = config.getTokenlimit();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, addDays);
		
		//DBに保存
		AutologinTblEntity autologinTblEntity = new AutologinTblEntity();
		
		autologinTblEntity.setToken(token);
		autologinTblEntity.setLmitDate(cal.getTime());
		autologinTblEntity.setUserId(userId);
		
		autoLoginRepository.save(autologinTblEntity);
		
		return token;
		
	}

	/**
	 * トークンを認証する
	 * @param token
	 * @return
	 */
	public LoginInfoDto authToken(String token) {
		LoginInfoDto dto = null;
		
		AutologinTblEntity autologinTblEntity = autoLoginRepository.getTokenInfo(token);
		
		if(autologinTblEntity != null && autologinTblEntity.getUserTbl() != null) {
			UserTblEntity entity = autologinTblEntity.getUserTbl();

			//entity -> dto
			dto =getFrom(entity);
			
			//いったん個のトークンは削除（新しいトークンを発行する）
			autoLoginRepository.delete(autologinTblEntity);
		}
		
		//有効期限切れのトークンを削除する
		List<AutologinTblEntity> lmitList = autoLoginRepository.getLimitTokenInfo();
		if( lmitList.size() > 0 ) {
			autoLoginRepository.deleteAll(lmitList);
		}
		
		return dto;
	}
	
	/**
	 * ログアウト処理
	 * 
	 * @param token
	 */
	public void logout(String token) {

		AutologinTblEntity autologinTblEntity = autoLoginRepository.getTokenInfo(token);
		if( autologinTblEntity != null ) {
			autoLoginRepository.delete(autologinTblEntity);
		}
	}

	/**
	 * 指定したメールアドレスが既に存在するかどうかを検査する
	 * 
	 * @param mail
	 * @return
	 */
	public boolean isExistMailadress(String mail) {
		return ( userRepository.getUserByMail(mail) != null ? true:false);
	}
	
	
	/**
	 * 学籍番号が存在するかどうかを返す
	 * @param studentNo
	 * @return
	 */
	public boolean isExistStudentNo(String studentNo) {
		return ( userRepository.getUserByStudentNo(studentNo) != null ? true:false);
	}
	
	/**
	 * EntityからDTOを作成する
	 * @param entity
	 * @return
	 */
	private LoginInfoDto getFrom(UserTblEntity entity) {

		LoginInfoDto dto = new LoginInfoDto();

		dto.setUserId(entity.getUserId());
		dto.setMail(entity.getMail());
		dto.setGrade(entity.getGrade());
		dto.setNickName(entity.getNickName());
		dto.setRole( RoleId.search(entity.getRole()) );
		dto.setRoleName(RoleId.search(entity.getRole()).getMsg());
		dto.setCourseName( entity.getHomeroomTbl().getName() );
		dto.setGrade(entity.getGrade());
		dto.setLevelName(entity.getLevelTbl().getName());
		dto.setAvater(entity.getAvater());
		dto.setFollowNum(entity.getFollowNum());
		dto.setFollowerNum(entity.getFollowerNum());
		dto.setGoodNum(entity.getGoodNum());
		dto.setSubmittedQNum(0);
		
		return dto;
	}
	
	/**
	 * @param createUserDto
	 * @param isNew
	 * @return
	 * @throws AZCafeException
	 */
	private UserTblEntity getFrom(CreateUserDto createUserDto,boolean isNew) throws AZCafeException {
		UserTblEntity entity = new UserTblEntity();

		//ハッシュ計算する
		String hashedPwd  = Digest.createPassword(createUserDto.getMail(), createUserDto.getPass());
		entity.setPassword(hashedPwd);
		
		entity.setAvater(createUserDto.getIconFileName());
		entity.setRole(createUserDto.getRoleId());
		entity.setOrgNo(createUserDto.getStudentNo());
		entity.setName(createUserDto.getNickname());
		entity.setNickName(createUserDto.getNickname());
		entity.setMail(createUserDto.getMail());
		entity.setEnterYear(createUserDto.getAdmissionYear());
		entity.setIntroduction("");
		
		HomeroomTblEntity homeroomTblEntity = new HomeroomTblEntity();
		homeroomTblEntity.setHomeroomId(createUserDto.getHomeroomId());
		entity.setHomeroomTbl(homeroomTblEntity);
		
		if( isNew ) {
			LevelTblEntity levelTblEntity = new LevelTblEntity();
			levelTblEntity.setLevelId(1);
			entity.setLevelTbl(levelTblEntity);
			entity.setFollowerNum(0);
			entity.setFollowNum(0);
			entity.setGoodNum(0);
			entity.setGrade(1);
			entity.setPoint(0);
		}
		
		return entity;
	}

}
