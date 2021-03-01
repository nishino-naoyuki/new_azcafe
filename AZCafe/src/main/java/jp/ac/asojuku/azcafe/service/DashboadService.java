package jp.ac.asojuku.azcafe.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.azcafe.config.MessageProperty;
import jp.ac.asojuku.azcafe.dto.DashBoadDto;
import jp.ac.asojuku.azcafe.dto.InfomationDto;
import jp.ac.asojuku.azcafe.dto.LevelDto;
import jp.ac.asojuku.azcafe.entity.AnswerGoodTblEntity;
import jp.ac.asojuku.azcafe.entity.AnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.CommentTblEntity;
import jp.ac.asojuku.azcafe.entity.FollowTblEntity;
import jp.ac.asojuku.azcafe.entity.UserLevelTblEntity;
import jp.ac.asojuku.azcafe.entity.UserTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.InfoKind;
import jp.ac.asojuku.azcafe.param.LevelRank;
import jp.ac.asojuku.azcafe.param.RoleId;
import jp.ac.asojuku.azcafe.repository.AnswerGoodRepository;
import jp.ac.asojuku.azcafe.repository.AnswerRepository;
import jp.ac.asojuku.azcafe.repository.AssignmentRepository;
import jp.ac.asojuku.azcafe.repository.CommentRepository;
import jp.ac.asojuku.azcafe.repository.FollowRepository;
import jp.ac.asojuku.azcafe.repository.UserRepository;

@Service
public class DashboadService {

	@Autowired
	AnswerRepository answerRepository;
	@Autowired
	AnswerGoodRepository answerGoodRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	FollowRepository followRepository;
	@Autowired
	AssignmentRepository assignmentRepository;
	@Autowired
	CommentRepository commentRepository;
	
	public DashBoadDto getData(Integer userId) throws AZCafeException {
		DashBoadDto dashBoadDto = new DashBoadDto();
		
		/////////////////////////////////
		//ユーザーに関する情報
		UserTblEntity userEntity = setUserInfo(userId,dashBoadDto);

		/////////////////////////////////
		//お知らせ情報
		setInfomation(userEntity,dashBoadDto);
		
		return dashBoadDto;
	}
	
	private void setInformationByFollowAction(UserTblEntity userEntity,DashBoadDto dashBoadDto) throws AZCafeException {
		int userId = userEntity.getUserId();
		//フォロー中の人の中でコメントを最近した人を取得する
		List<CommentTblEntity> commentList = 
				commentRepository.getCommentByFollowRecentry(userId);
		//フォロー中の人の中でイイネを最近した人を取得する
		List<AnswerGoodTblEntity> goodList =
				answerGoodRepository.getGoodCountByFollowRecently(userId);

		String msgFmt = MessageProperty.getInstance().getProperty(MessageProperty.INFO_RECENT_COMMENT_FOLLOW);
		for(CommentTblEntity entity : commentList ) {
			InfomationDto dto = new InfomationDto();
			AnswerTblEntity answerEntity = answerRepository.getOne(entity.getAnswerId());
			
			dto.setInfoKind(InfoKind.COMMENT);
			dto.setNew(true);
			dto.setDate(entity.getEntryDate());
			dto.setMessage(
					String.format(msgFmt, 
							entity.getUserId(),entity.getUserTbl().getNickName(),
							answerEntity.getUserId(),answerEntity.getUserTbl().getNickName(),
							answerEntity.getAssignmentId(),answerEntity.getAssignmentTbl().getTitle())
					);
			dashBoadDto.addInfomationDto(dto);
		}

		/////////////////////////////////
		//いいね
		msgFmt = MessageProperty.getInstance().getProperty(MessageProperty.INFO_RECENT_GOOD_FOLLOW);
		for(AnswerGoodTblEntity entity : goodList ) {
			InfomationDto dto = new InfomationDto();
			AnswerTblEntity answerEntity = answerRepository.getOne(entity.getAnswerId());
			dto.setInfoKind(InfoKind.GOOD);
			dto.setNew(true);
			dto.setDate(entity.getGoodDate());
			dto.setMessage(
					String.format(msgFmt, 
							entity.getUserId(),entity.getUserTbl().getNickName(),
							answerEntity.getUserId(),answerEntity.getUserTbl().getNickName(),
							answerEntity.getAssignmentId(),answerEntity.getAssignmentTbl().getTitle())
					);			
			dashBoadDto.addInfomationDto(dto);
		}
	}
	
	private void setInformationToMyself(UserTblEntity userEntity,DashBoadDto dashBoadDto) throws AZCafeException{
		int userId = userEntity.getUserId();

		List<CommentTblEntity> commentList = 
				commentRepository.getCommentRecentry(userId);
		List<FollowTblEntity> followerList =
				followRepository.getRecentlyFollowerUser(userId);
		List<AnswerGoodTblEntity> goodList =
				answerGoodRepository.getGoodCountRecently(userId);
		List<AssignmentTblEntity> newAssignmentList =
				assignmentRepository.getRecentlyNewAssignment(userEntity.getHomeroomTbl().getHomeroomId());

		/////////////////////////////////
		//コメント
		String msgFmt = MessageProperty.getInstance().getProperty(MessageProperty.INFO_RECENT_COMMENT);
		for(CommentTblEntity entity : commentList ) {
			InfomationDto dto = new InfomationDto();
			AnswerTblEntity answerEntity = answerRepository.getOne(entity.getAnswerId());
			
			dto.setInfoKind(InfoKind.COMMENT);
			dto.setNew(true);
			dto.setDate(entity.getEntryDate());
			dto.setMessage(
					String.format(msgFmt, 
							answerEntity.getAssignmentId(),answerEntity.getAssignmentTbl().getTitle(),
							entity.getUserId(),entity.getUserTbl().getNickName())
					);
			dashBoadDto.addInfomationDto(dto);
		}
		/////////////////////////////////
		//フォロワー
		msgFmt = MessageProperty.getInstance().getProperty(MessageProperty.INFO_RECENT_FOLLOWER);
		for(FollowTblEntity entity : followerList ) {
			InfomationDto dto = new InfomationDto();
			
			dto.setInfoKind(InfoKind.FOLLOW);
			dto.setNew(true);
			dto.setDate(entity.getFollowDate());
			dto.setMessage(
					String.format(msgFmt, entity.getUserId(),entity.getUserTbl().getNickName())
					);			
			dashBoadDto.addInfomationDto(dto);
		}
		/////////////////////////////////
		//いいね
		msgFmt = MessageProperty.getInstance().getProperty(MessageProperty.INFO_RECENT_GOOD);
		for(AnswerGoodTblEntity entity : goodList ) {
			InfomationDto dto = new InfomationDto();
			AssignmentTblEntity assignmentEntity = assignmentRepository.getAssignmentByAnsId(entity.getAnswerId());
			dto.setInfoKind(InfoKind.GOOD);
			dto.setNew(true);
			dto.setDate(entity.getGoodDate());
			dto.setMessage(
					String.format(msgFmt, assignmentEntity.getAssignmentId(),assignmentEntity.getTitle())
					);			
			dashBoadDto.addInfomationDto(dto);
		}
		/////////////////////////////////
		//新規問題
		msgFmt = MessageProperty.getInstance().getProperty(MessageProperty.INFO_RECENT_CREATE);
		for(AssignmentTblEntity entity : newAssignmentList ) {
			InfomationDto dto = new InfomationDto();
			dto.setInfoKind(InfoKind.ADD);
			dto.setNew(true);
			dto.setDate(entity.getCreateDate());
			dto.setMessage(
					String.format(msgFmt, entity.getTitle())
					);			
			dashBoadDto.addInfomationDto(dto);
		}
	}
	private void setInfomation(UserTblEntity userEntity,DashBoadDto dashBoadDto) throws AZCafeException {
		
		//自分への行動のお知らせ情報
		setInformationToMyself(userEntity,dashBoadDto);
		//フォロワーが行った行動のお知らせ情報
		setInformationByFollowAction(userEntity,dashBoadDto);
		
		dashBoadDto.sortInfomation();
	}
	
	private UserTblEntity setUserInfo(Integer userId,DashBoadDto dashBoadDto) {
		UserTblEntity userEntity = userRepository.getOne(userId);
		int followNum = followRepository.getFollowCount(userId);
		int followerNum = followRepository.getFollowerCount(userId);
		int goodNum = answerGoodRepository.getGoodCount(userId);
		int assignmentNum = 0;
		
		if( RoleId.TEACHER.equals(userEntity.getRole())) {
			assignmentNum = assignmentRepository.getAssignmentCountTeacher(userEntity.getHomeroomTbl().getHomeroomId());
		}else {
			assignmentNum = assignmentRepository.getAssignmentCount(userEntity.getHomeroomTbl().getHomeroomId());
		}
		List<AnswerTblEntity> answerList = answerRepository.getList(userId);
		
		//dtoにセット
		dashBoadDto.setAnswerNum(answerList.size());
		dashBoadDto.setAssignmentNum(assignmentNum);
		dashBoadDto.setFollowNum(followNum);
		dashBoadDto.setFollowerNum(followerNum);
		dashBoadDto.setGoodNum(goodNum);
		dashBoadDto.setPoint(userEntity.getPoint());
		//称号をセット
		for( UserLevelTblEntity entity : userEntity.getUserLevelSet() ) {
			LevelDto lvDto = new LevelDto();
			lvDto.setLevelId(entity.getLevelTbl().getLevelId());
			lvDto.setName(entity.getLevelTbl().getName());
			lvDto.setRank( LevelRank.getBy(entity.getLevelTbl().getLevel()) );
			dashBoadDto.addLevelList(lvDto);
		}
		
		return userEntity;
	}
	
}
