package jp.ac.asojuku.azcafe.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.dto.CreateUserDto;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.dto.UserInfoDto;
import jp.ac.asojuku.azcafe.dto.UserSearchElementDto;
import jp.ac.asojuku.azcafe.dto.subclass.AssignmentResultDto;
import jp.ac.asojuku.azcafe.dto.subclass.FollowElementDto;
import jp.ac.asojuku.azcafe.entity.AnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.AutologinTblEntity;
import jp.ac.asojuku.azcafe.entity.FollowTblEntity;
import jp.ac.asojuku.azcafe.entity.FollowTblId;
import jp.ac.asojuku.azcafe.entity.HomeroomTblEntity;
import jp.ac.asojuku.azcafe.entity.LevelTblEntity;
import jp.ac.asojuku.azcafe.entity.UserTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.form.UserSearchConditionForm;
import jp.ac.asojuku.azcafe.param.RoleId;
import jp.ac.asojuku.azcafe.repository.AnswerRepository;
import jp.ac.asojuku.azcafe.repository.AutoLoginRepository;
import jp.ac.asojuku.azcafe.repository.FollowRepository;
import jp.ac.asojuku.azcafe.repository.UserRepository;
import jp.ac.asojuku.azcafe.util.Digest;
import jp.ac.asojuku.azcafe.util.Token;
import static jp.ac.asojuku.azcafe.repository.UserSpecifications.*;

@Service
public class UserService {

	@Autowired 
	UserRepository userRepository;
	@Autowired
	AutoLoginRepository autoLoginRepository;
	@Autowired
	FollowRepository followRepository;
	@Autowired
	AnswerRepository answerRepository;
	@Autowired
	AZCafeConfig config;

	/**
	 * フォロー情報を登録する
	 * @param userId			フォローする人のID
	 * @param targetUserId　フォローされる人のID
	 * @return　
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean insertFollow(Integer userId,Integer targetUserId) {
		FollowTblEntity entity = new FollowTblEntity();
		
		entity.setUserId(userId);
		entity.setFollewUserId(targetUserId);
		
		followRepository.save(entity);
		
		//フォロー数を更新
		Integer followNum = followRepository.getFollowCount(userId);
		UserTblEntity userEntity = userRepository.getOne(userId);
		userEntity.setFollowNum(followNum);
		userRepository.save(userEntity);
		
		//フォローしたユーザーのフォロワー数も更新
		Integer followerNum = followRepository.getFollowerCount(targetUserId);
		UserTblEntity fUerEntity = userRepository.getOne(targetUserId);
		fUerEntity.setFollowerNum(followerNum);
		userRepository.save(fUerEntity);
		
		return true;
	}
	/**
	 * ユーザー情報を取得する
	 * @param userId
	 * @return
	 */
	public UserInfoDto getInfo(Integer loginUserId,Integer userId) {
		UserInfoDto userInfo = new UserInfoDto();
		
		UserTblEntity entity = userRepository.getOne(userId);
		
		userInfo = getUserInfoDtoFrom(loginUserId,entity);
		
		return userInfo;
	}
	
	private UserInfoDto getUserInfoDtoFrom(Integer loginUserId,UserTblEntity entity) {
		UserInfoDto userInfo = new UserInfoDto();
		
		//基本情報をセット
		userInfo.setUserId(entity.getUserId());
		userInfo.setOrgNo(entity.getOrgNo());
		userInfo.setNickName(entity.getNickName());
		userInfo.setAvater(entity.getAvater());
		userInfo.setFollowNum(entity.getFollowNum());
		userInfo.setFollowerNum(entity.getFollowerNum());
		userInfo.setLevel(entity.getLevelTbl().getName());
		userInfo.setHomeroomeName( entity.getHomeroomTbl().getName() );
		userInfo.setRole( RoleId.search(entity.getRole()) );
		if(loginUserId != entity.getUserId() ) {
			//ログインユーザーと違うユーザーを表示するときはそのユーザーをフォローしているかどうかを取得する
			FollowTblEntity fEntity = followRepository.findById(new FollowTblId(loginUserId,entity.getUserId())).orElse(null);
			userInfo.setIsFollowUser((fEntity!=null));
		}else {
			userInfo.setIsFollowUser(true);
		}
		//フォローリスト
		List<FollowTblEntity> followList = followRepository.getFollowUser(entity.getUserId());
		for( FollowTblEntity followEnity : followList) {
			FollowElementDto dto = new FollowElementDto();
			UserTblEntity userEntity = followEnity.getFollewUserTbl();
			dto.setUserId( userEntity.getUserId() );
			dto.setNickName( userEntity.getNickName() );
			dto.setAvater( userEntity.getAvater() );
			dto.setHoomroomName( userEntity.getHomeroomTbl().getName() );
			
			userInfo.addFollowElementDto(dto);
		}
		//フォロワーリスト　自分を「フォローしている」を検索
		List<FollowTblEntity> followerList = followRepository.getFollowerUser(entity.getUserId());
		for( FollowTblEntity followEnity : followerList) {
			FollowElementDto dto = new FollowElementDto();
			UserTblEntity userEntity = followEnity.getUserTbl();	//「誰が」を取得
			dto.setUserId( userEntity.getUserId() );
			dto.setNickName( userEntity.getNickName() );
			dto.setAvater( userEntity.getAvater() );
			dto.setHoomroomName( userEntity.getHomeroomTbl().getName() );			
			FollowTblEntity fEntity = followRepository.findById(new FollowTblId(entity.getUserId(),followEnity.getUserId())).orElse(null);
			dto.setIsEach((fEntity != null));
			
			userInfo.addFollowerElementDto(dto);
		}
		//解いた問題取得
		List<AnswerTblEntity> answerList = answerRepository.getList(entity.getUserId());
		for( AnswerTblEntity answerEntity : answerList) {
			AssignmentResultDto dto = new AssignmentResultDto();
			
			dto.setAssigmentId(answerEntity.getAssignmentId());
			dto.setTitle(answerEntity.getAssignmentTbl().getTitle());
			dto.setGroup(answerEntity.getAssignmentTbl().getGroupTbl().getName());
			dto.setScore(answerEntity.getScore());
			dto.setCommentNum(answerEntity.getCommentTblSet().size());
			dto.setGoodNum(answerEntity.getAnswerGoodTblSet().size());
			if(loginUserId != entity.getUserId() ) {
				//ログインユーザーと違う時は、どの問題を提出済みかを取得する
				dto.setIsCorrect(isCorrect(loginUserId,answerEntity.getAssignmentId()));
			}else {
				dto.setIsCorrect(true);
			}
			//dto.setUpdateDate(answerEntity.get);
			
			userInfo.addAssignmentResultDto(dto);
		}
		
		return userInfo;
	}
	
	private boolean isCorrect(Integer userId,Integer assigmentId) {
		AnswerTblEntity answerEntity = answerRepository.getOne(assigmentId, userId);
		
		if(answerEntity == null || answerEntity.getAssignmentId() == null) {
			return false;
		}else {
			return true;
		}
		
	}
	
	/**
	 * 指定された条件でユーザーを検索する
	 * 
	 * @param cond
	 * @return
	 */
	public List<UserSearchElementDto> getList(UserSearchConditionForm cond){
		List<UserSearchElementDto> list = new ArrayList<>();
		

		List<UserTblEntity> entityList = userRepository.findAll(
				Specification.
						where(mailContains(cond.getMail())).
						and(homeroomEquals(cond.getHomeroomId())).
						and(nicknameContains(cond.getNickname())).
						and(roleEquals(cond.getRoleId())).
						and(levelEquals(cond.getLevel())),
						Sort.by(Sort.Direction.ASC, "mail")
				);
		
		//entity -> dto
		for(UserTblEntity entity : entityList ) {
			UserSearchElementDto dto = new UserSearchElementDto();
			
			dto.setUserId(entity.getUserId());
			dto.setNickName(entity.getNickName());
			dto.setOrgNo(entity.getOrgNo());
			dto.setHomeroomeName(entity.getHomeroomTbl().getName());
			dto.setLevel(entity.getLevelTbl().getName());
			dto.setAvater(entity.getAvater());
			dto.setFollowNum(entity.getFollowNum());
			dto.setFollowerNum(entity.getFollowerNum());
			
			list.add(dto);
		}
		
		return list;
	}
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
