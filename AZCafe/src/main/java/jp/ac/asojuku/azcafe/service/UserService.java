package jp.ac.asojuku.azcafe.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.dto.CreateUserDto;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.dto.RankingDto;
import jp.ac.asojuku.azcafe.dto.SkillMapDto;
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
import jp.ac.asojuku.azcafe.entity.SkillMapTblEntity;
import jp.ac.asojuku.azcafe.entity.SkillTblEntity;
import jp.ac.asojuku.azcafe.entity.UserTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.form.UserSearchConditionForm;
import jp.ac.asojuku.azcafe.param.RoleId;
import jp.ac.asojuku.azcafe.repository.AnswerRepository;
import jp.ac.asojuku.azcafe.repository.AutoLoginRepository;
import jp.ac.asojuku.azcafe.repository.FollowRepository;
import jp.ac.asojuku.azcafe.repository.SkillRepository;
import jp.ac.asojuku.azcafe.repository.UserRepository;
import jp.ac.asojuku.azcafe.util.Digest;
import jp.ac.asojuku.azcafe.util.FileUtils;
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
	@Autowired
	SkillRepository skillRepository;

	/**
	 * ランキングを取得する
	 * @param homeroomId
	 * @return
	 */
	public List<RankingDto> getRanking(Integer homeroomId) {
		List<RankingDto> rankingList = new ArrayList<>();

		List<UserTblEntity> entityList = userRepository.findAll(
				Specification.
						where(homeroomEquals(homeroomId)).
						and(roleEquals(RoleId.STUDENT.getId())),
						Sort.by(Sort.Direction.DESC, "point").
						and(Sort.by(Sort.Direction.ASC,"homeroomTbl.homeroomId")).
						and(Sort.by(Sort.Direction.ASC,"orgNo"))
				);
		
		int rank = 1;
		int count = 1;
		int prePoint = -1;
		//entity -> dto
		for(UserTblEntity userEntity : entityList ) {
			RankingDto dto = new RankingDto();
			
			dto.setUserId(userEntity.getUserId());
			dto.setNickName(userEntity.getNickName());
			dto.setHomeroomeName(userEntity.getHomeroomTbl().getName());
			dto.setAvater(userEntity.getAvater());
			dto.setName(userEntity.getName());
			dto.setOrgNo(userEntity.getOrgNo());
			dto.setPoint(userEntity.getPoint());
			if( userEntity.getPoint() != prePoint) {
				rank = count;
			}
			dto.setRank(rank);	
			prePoint = userEntity.getPoint();
			count++;
			
			rankingList.add(dto);
		}
		
		return rankingList;
	}
	/**
	 * アイコンを更新する
	 * @param userId
	 * @param iconPath
	 */
	public void updateIcon(Integer userId,String iconPath) {

		UserTblEntity userEntity = userRepository.getOne(userId);
		
		String oldIconPath = userEntity.getAvater();
		
		userEntity.setAvater(iconPath);
		userRepository.save(userEntity);

		if( StringUtils.isNotEmpty(oldIconPath)) {
			//旧ファイルを削除する
			FileUtils.deleteIconFile(oldIconPath);
		}
	}
	/**
	 * パスワードの更新
	 * 
	 * @param userId
	 * @param password
	 * @throws AZCafeException
	 */
	public void updatePassword(Integer userId,String password) throws AZCafeException {

		UserTblEntity userEntity = userRepository.getOne(userId);
		
		//ハッシュ計算する
		String hashedPwd  = Digest.createPassword(userEntity.getMail(), password);
		userEntity.setPassword(hashedPwd);

		userRepository.save(userEntity);
	}

	/**
	 * パスワードの更新
	 * 
	 * @param userId
	 * @param password
	 * @throws AZCafeException
	 */
	public void updatePassword(String mail,String password) throws AZCafeException {

		UserTblEntity userEntity = userRepository.getUserByMail(mail);
		
		//ハッシュ計算する
		String hashedPwd  = Digest.createPassword(userEntity.getMail(), password);
		userEntity.setPassword(hashedPwd);

		userRepository.save(userEntity);
	}
	/**
	 * ニックネームの更新
	 * 
	 * @param userId
	 * @param nickName
	 */
	public void updateNickName(Integer userId,String nickName) {
		UserTblEntity userEntity = userRepository.getOne(userId);
		
		userEntity.setNickName(nickName);
		
		userRepository.save(userEntity);
	}
	/**
	 * フォローの解除
	 * 
	 * @param userId　フォロー解除する人（誰が）
	 * @param targetUserId　フォロー解除される人（誰を）
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean removeFollow(Integer userId,Integer targetUserId) {
		
		//削除
		followRepository.deleteById( new FollowTblId(userId,targetUserId) );

		//フォロー数を更新
		Integer followNum = followRepository.getFollowCount(userId);
		UserTblEntity userEntity = userRepository.getOne(userId);
		userEntity.setFollowNum(followNum);
		userRepository.save(userEntity);
		
		//フォロー解除ユーザーのフォロワー数も更新
		Integer followerNum = followRepository.getFollowerCount(targetUserId);
		UserTblEntity fUerEntity = userRepository.getOne(targetUserId);
		fUerEntity.setFollowerNum(followerNum);
		userRepository.save(fUerEntity);
		
		return true;
	}
	/**
	 * フォロー情報を登録する
	 * @param userId			フォローする人のID（誰が）
	 * @param targetUserId　フォローされる人のID（誰を）
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
	 * userIdでしていたユーザー情報を取得する
	 * @param userId
	 * @return
	 */
	public UserInfoDto getInfo(Integer loginUserId,Integer userId) {
		UserInfoDto userInfo = new UserInfoDto();
		
		UserTblEntity entity = userRepository.getOne(userId);
		
		userInfo = getUserInfoDtoFrom(loginUserId,entity);
		
		return userInfo;
	}
	
	/**
	 * ユーザー情報DTOをEnityから取得する
	 * 
	 * @param loginUserId
	 * @param entity
	 * @return
	 */
	private UserInfoDto getUserInfoDtoFrom(Integer loginUserId,UserTblEntity entity) {
		UserInfoDto userInfo = new UserInfoDto();
		
		//基本情報をセット
		userInfo.setUserId(entity.getUserId());
		userInfo.setOrgNo(entity.getOrgNo());
		userInfo.setNickName(entity.getNickName());
		userInfo.setName(entity.getName());
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
			UserTblEntity userEntity =userRepository.getOne( followEnity.getFollewUserId() );
			dto.setUserId( userEntity.getUserId() );
			dto.setNickName( userEntity.getNickName() );
			dto.setAvater( userEntity.getAvater() );
			dto.setHoomroomName( userEntity.getHomeroomTbl().getName() );
			FollowTblEntity fEntity = followRepository.findById(new FollowTblId(loginUserId,followEnity.getFollewUserId())).orElse(null);
			dto.setIsEach((fEntity != null));
			
			userInfo.addFollowElementDto(dto);
		}
		//フォロワーリスト　自分を「フォローしている」を検索
		List<FollowTblEntity> followerList = followRepository.getFollowerUser(entity.getUserId());
		for( FollowTblEntity followEnity : followerList) {
			FollowElementDto dto = new FollowElementDto();
			//「誰が」を取得
			UserTblEntity userEntity =userRepository.getOne( followEnity.getUserId() );
			dto.setUserId( userEntity.getUserId() );
			dto.setNickName( userEntity.getNickName() );
			dto.setAvater( userEntity.getAvater() );
			dto.setHoomroomName( userEntity.getHomeroomTbl().getName() );	
			FollowTblEntity fEntity;
			if( loginUserId != entity.getUserId() ) {
				fEntity = followRepository.findById(new FollowTblId(loginUserId,followEnity.getUserId())).orElse(null);
			}else {
				fEntity = followRepository.findById(new FollowTblId(loginUserId,followEnity.getUserId())).orElse(null);
			}
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
			dto.setAnswerDate(answerEntity.getAnswerDate());
			if(loginUserId != entity.getUserId() ) {
				//ログインユーザーと違う時は、どの問題を提出済みかを取得する
				dto.setIsCorrect(isCorrect(loginUserId,answerEntity.getAssignmentId()));
			}else {
				dto.setIsCorrect(true);
			}
			//dto.setUpdateDate(answerEntity.get);
			
			userInfo.addAssignmentResultDto(dto);
		}
		//スキルマップ
		List<SkillTblEntity> skillAllList = skillRepository.findAll();
		Set<SkillMapTblEntity> skillMapSet = entity.getSkillMapTblSet();
		for( SkillTblEntity skillEntity : skillAllList ) {
			int point = containSkill(skillEntity.getSkillId(),skillMapSet);
			SkillMapDto skillMapDto = new SkillMapDto();
			skillMapDto.setSkillId(skillEntity.getSkillId());
			skillMapDto.setPoint(point);
			skillMapDto.setName(skillEntity.getName());
			
			userInfo.addSkillMapDto(skillMapDto);
		}
		
		return userInfo;
	}
	
	private int containSkill(Integer skillId,Set<SkillMapTblEntity> skillMapSet) {
		int point = 0;
		for( SkillMapTblEntity skillmap : skillMapSet) {
			if( skillmap.getSkillId() == skillId ) {
				point = skillmap.getPoint();
				break;
			}
		}
		
		return point;
	}
	
	/**
	 * 提出済みかどうかを取得する
	 * 
	 * @param userId
	 * @param assigmentId
	 * @return
	 */
	private boolean isCorrect(Integer userId,Integer assigmentId) {
		AnswerTblEntity answerEntity = answerRepository.getOne(assigmentId, userId);
		
		if(answerEntity == null || answerEntity.getAssignmentId() == null) {
			return false;
		}else {
			return (answerEntity.getCorrectFlg() == 1);
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
						and(nameContains(cond.getName())).
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
			dto.setName(entity.getName());
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
		dto.setHomeroomId(entity.getHomeroomTbl().getHomeroomId());
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
		entity.setName(createUserDto.getName());
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
