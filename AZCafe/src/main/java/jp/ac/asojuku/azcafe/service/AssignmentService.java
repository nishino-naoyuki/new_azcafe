package jp.ac.asojuku.azcafe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.azcafe.dto.AssignmentTestCaseDto;
import jp.ac.asojuku.azcafe.dto.GradingResultDto;
import jp.ac.asojuku.azcafe.dto.GradingTestCaseResultDto;
import jp.ac.asojuku.azcafe.dto.PublicAssignmentDto;
import jp.ac.asojuku.azcafe.dto.SkillDto;
import jp.ac.asojuku.azcafe.dto.AssignmentDetailDto;
import jp.ac.asojuku.azcafe.dto.AssignmentDto;
import jp.ac.asojuku.azcafe.dto.AssignmentElementDto;
import jp.ac.asojuku.azcafe.dto.AssignmentPublicDto;
import jp.ac.asojuku.azcafe.entity.AnswerDetailTblEntity;
import jp.ac.asojuku.azcafe.entity.AnswerGoodTblEntity;
import jp.ac.asojuku.azcafe.entity.AnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.FollowTblEntity;
import jp.ac.asojuku.azcafe.entity.FollowTblId;
import jp.ac.asojuku.azcafe.entity.GroupTblEntity;
import jp.ac.asojuku.azcafe.entity.PublicAssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.SkillAssTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseAnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseTblEntity;
import jp.ac.asojuku.azcafe.param.Difficulty;
import jp.ac.asojuku.azcafe.repository.AnswerGoodRepository;
import jp.ac.asojuku.azcafe.repository.AnswerRepository;
import jp.ac.asojuku.azcafe.repository.AssignmentRepository;
import jp.ac.asojuku.azcafe.repository.FollowRepository;
import jp.ac.asojuku.azcafe.repository.GroupRepository;
import jp.ac.asojuku.azcafe.repository.PublicAssignmentRepository;
import jp.ac.asojuku.azcafe.repository.SkillAssRepository;
import jp.ac.asojuku.azcafe.repository.TestCaseRepository;

/**
 * 課題サービス
 * @author nishino
 *
 */
@Service
public class AssignmentService {

	@Autowired
	AnswerGoodRepository answerGoodRepository;
	@Autowired
	AssignmentRepository assignmentRepository;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	PublicAssignmentRepository publicAssignmentRepository;
	@Autowired
	TestCaseRepository testCaseRepository;
	@Autowired
	AnswerRepository answerRepository;
	@Autowired
	GradingService gradingService;
	@Autowired
	SkillAssRepository skillAssRepository;

	/**
	 * 公開情報を一括更新する
	 * 
	 * @param idList
	 * @param publicList
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public int updatePublicStates(List<Integer> idList, List<AssignmentPublicDto> publicList) {
		int count = 0;
		
		for(Integer assignmentId : idList) {
			AssignmentTblEntity assignmentEntity = assignmentRepository.getOne(assignmentId);
			//公開設定登録
			insertPublicState(publicList,assignmentEntity);
			count++;	//処理した件数
		}
		
		return count;
	}
	/**
	 * userIdで指定したユーザーがanswerIdで指定した解答にイイネを追加する
	 * すでに追加されている場合は削除する
	 * 
	 * @param answerId
	 * @param userId
	 * @return
	 */
	public int insertGood(Integer answerId,Integer userId) {
		//すでに登録しているかのチェック
		AnswerGoodTblEntity goodEntity = answerGoodRepository.getGoodBy(answerId, userId);
		if( goodEntity != null ) {
			//すでにある場合は削除する
			answerGoodRepository.delete(goodEntity);
		}else {
			//無い場合は登録
			goodEntity = new AnswerGoodTblEntity();
			goodEntity.setAnswerId(answerId);
			goodEntity.setUserId(userId);
			answerGoodRepository.save(goodEntity);
		}
		
		//この問題に対するイイネの数を取得する
		return answerGoodRepository.getGoodCountForAnswert(answerId);
	}
	/**
	 * 課題の詳細情報を取得する
	 * @param id
	 * @return
	 */
	public AssignmentDetailDto getDetail(Integer assignmentId,Integer userId) {
		if( assignmentId == null ) {
			return null;
		}
		AssignmentTblEntity entity = assignmentRepository.getOne(assignmentId);
		
		return getDetailFrom(userId,entity);
	}
	/**
	 * 課題リストを取得する
	 * @return
	 */
	public List<AssignmentElementDto> getAll(Integer userId){
		List<AssignmentElementDto> list = new ArrayList<>();
		
		List<AssignmentTblEntity> entityList = 
				assignmentRepository.findAll(Sort.by(Sort.Direction.ASC, "groupId","groupInNo"));
		
		for( AssignmentTblEntity entity : entityList ) {
			AssignmentElementDto dto = getFrom(userId,entity);
			list.add(dto);
		}
		
		return list;
	}
	
	/**
	 * @param assignmentId
	 * @return
	 */
	public AssignmentDto get(Integer assignmentId) {
		AssignmentTblEntity entity = assignmentRepository.getOne(assignmentId);
		AssignmentDto dto = new AssignmentDto();
		
		dto.setAssignmentId(assignmentId);
		dto.setGroup(entity.getGroupTbl().getName());
		dto.setTitle(entity.getTitle());
		dto.setContent(entity.getContents());
		dto.setDifficulty(entity.getDifficulty());
		for( PublicAssignmentTblEntity publicEntity :  entity.getPublicQuestionTblSet() ) {
			AssignmentPublicDto publicDto = new AssignmentPublicDto();
			
			publicDto.setHomeroomId(publicEntity.getHomeroomId());
			publicDto.setHomeroomName(publicEntity.getHomeroomTbl().getName());
			publicDto.setPublicState(publicEntity.getPublicState());
			
			dto.addAssignmentPublicDto(publicDto);
		}
		for(TestCaseTblEntity testCaseEntity : entity.getTestCaseTblSet()) {
			AssignmentTestCaseDto tetCaseDto = new AssignmentTestCaseDto();
			
			tetCaseDto.setInput(testCaseEntity.getInputText());
			tetCaseDto.setOutput(testCaseEntity.getOutputTxt());
			
			dto.addAssignmentTestCaseDto(tetCaseDto);
		}
		
		return dto;
	}
	/**
	 * 課題を挿入する
	 * @param dto
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertOrUpdate(AssignmentDto dto) {
		AssignmentTblEntity assignmentTblEntity = null;
		
		if( dto.getAssignmentId() != null ) {
			assignmentTblEntity = assignmentRepository.getOne(dto.getAssignmentId());
		}
		//グループ登録
		GroupTblEntity groupEntity = insertGroup(dto.getGroup());
		//本体登録
		assignmentTblEntity = insertAssignment(assignmentTblEntity,dto,groupEntity);
		//公開設定登録
		insertPublicState(dto.getPublicStateList(),assignmentTblEntity);
		//テストケース登録
		insertTestCase(dto,assignmentTblEntity);
		//スキルマップ登録
		insertSkillSetting(assignmentTblEntity,dto.getSkillIdList());
	}
	
	/**
	 * スキル設定を保存する
	 * 
	 * @param assignmentTblEntity
	 * @param skillIdList
	 */
	private void insertSkillSetting(AssignmentTblEntity assignmentTblEntity,List<Integer> skillIdList) {
		//いったん削除
		skillAssRepository.delete(assignmentTblEntity.getAssignmentId());
		//追加しなおす
		List<SkillAssTblEntity> insertList = new ArrayList<>();
		for(Integer skillId : skillIdList) {
			SkillAssTblEntity entity = new SkillAssTblEntity();
			
			entity.setAssignmentId(assignmentTblEntity.getAssignmentId());
			entity.setSkillId(skillId);
			
			insertList.add(entity);
		}
		
		skillAssRepository.saveAll(insertList);
	}
	
	/**
	 * グループの登録
	 * @param groupName
	 * @return
	 */
	private GroupTblEntity insertGroup(String groupName) {
		//問題グループ
		GroupTblEntity groupEntity = groupRepository.getGroupInfoBy(groupName);
		if( groupEntity == null ) {
			groupEntity = new GroupTblEntity();
			groupEntity.setName(groupName);
		}
		
		groupRepository.save(groupEntity);
		
		return groupEntity;
	}
	
	/**
	 * 課題の新規登録
	 * 
	 * @param dto
	 * @param groupEntity
	 * @return
	 */
	private AssignmentTblEntity insertAssignment(AssignmentTblEntity entity,AssignmentDto dto,GroupTblEntity groupEntity) {

		boolean updateDifficulty = false;	//難易度変更フラグ
		
		if( dto == null ) {
			return new AssignmentTblEntity();
		}
		if( entity == null ) {
			entity = new AssignmentTblEntity();
		}else {
			updateDifficulty = (entity.getDifficulty() != dto.getDifficultyAsInt());
		}
		
		entity.setTitle(dto.getTitle());
		entity.setContents(dto.getContent());	
		entity.setDifficulty(dto.getDifficultyAsInt());
		entity.setGood(0);
		entity.setGroupInNo(1);//TODO:要調整
		entity.setCreateUserId(0);//TODO:要調整
		entity.setUpdateUserId(0);//TODO:要調整
		//問題グループ
		entity.setGroupId(groupEntity.getGroupId());
		
		entity = assignmentRepository.save(entity);
		
		//難易度が変更になった場合は現在解答済みの答えのポイントを変更する
		if( updateDifficulty ) {
			updatePoint(entity);
		}
		
		return entity;
	}
	
	/**
	 * 難易度が変更になったときにポイントを更新する
	 * 
	 * @param entity
	 */
	private void updatePoint(AssignmentTblEntity entity) {
		
		List<AnswerTblEntity> ansList =  answerRepository.getListByAssId(entity.getAssignmentId());
		//答えの更新
		for(AnswerTblEntity ansEntity : ansList ) {
			int point = gradingService.calPoint(ansEntity, (ansEntity.getCorrectFlg()==1));
			ansEntity.setPoint(point);
			answerRepository.save(ansEntity);
			//ユーザーの情報も更新
			gradingService.updateUserTotalPoint(ansEntity.getUserId());
		}
				
	}
	
	/**
	 * 公開情報の登録
	 * 
	 * @param dto
	 * @param assignmentTblEntity
	 */
	private void insertPublicState(List<AssignmentPublicDto> publicList,AssignmentTblEntity assignmentTblEntity) {
		List<PublicAssignmentTblEntity> publicStateList = new ArrayList<>();
		
		//一旦今のものを削除する
		publicAssignmentRepository.delete(assignmentTblEntity.getAssignmentId());
		//登録しなおす
		for( AssignmentPublicDto publicDto : publicList) {
			PublicAssignmentTblEntity publicEntity = new PublicAssignmentTblEntity();
			
			publicEntity.setHomeroomId(publicDto.getHomeroomId());
			publicEntity.setAssignmentId(assignmentTblEntity.getAssignmentId());
			publicEntity.setPublicState(publicDto.getPublicState());
			
			publicStateList.add(publicEntity);
		}
		
		publicAssignmentRepository.saveAll(publicStateList);
	}
	
	/**
	 * テストケースの登録
	 * @param dto
	 * @param assignmentTblEntity
	 */
	private void insertTestCase(AssignmentDto dto,AssignmentTblEntity assignmentTblEntity) {
		List<TestCaseTblEntity> testCaseList = new ArrayList<>();
		
		if( !isChangeTestCase(
				dto.getAnswerList(),assignmentTblEntity.getTestCaseTblSet() ) ) {
			//変更なし！の場合は何もしない
			return;
		}		
		//////////////////////////////////////////////////////
		//変更がある場合は　解凍の削除→テストケースの削除→テストケース登録しなおし　をする
		//解答の削除
		answerRepository.delete(assignmentTblEntity.getAssignmentId());
		//テストケースの削除
		testCaseRepository.delete(assignmentTblEntity.getAssignmentId());		
		//再登録
		int no = 1;
		for( AssignmentTestCaseDto testcase : dto.getAnswerList()) {
			TestCaseTblEntity testcaseEntity = new TestCaseTblEntity();
			
			testcaseEntity.setAssignmentId(assignmentTblEntity.getAssignmentId());
			testcaseEntity.setInputText(testcase.getInput());
			testcaseEntity.setOutputTxt(testcase.getOutput());
			testcaseEntity.setNo(no++);
			
			testCaseList.add(testcaseEntity);
		}
		
		testCaseRepository.saveAll(testCaseList);
	}
	
	/**
	 * テストケースに変更があるかをチェックする
	 * @param newTestCaseList
	 * @param nowTestCaseSet
	 * @return
	 */
	private boolean isChangeTestCase(
			List<AssignmentTestCaseDto> newTestCaseList,
			Set<TestCaseTblEntity> nowTestCaseSet) {
		if( nowTestCaseSet == null ) {
			return true;	//nowTestCaseSetがない時は新規登録
		}
		boolean isChange = false;
		
		//数に変更があるときは変更有！
		if( newTestCaseList.size() != nowTestCaseSet.size() ) {
			return true;
		}
		
		//数も同じときはひとつずつ比べる！
		for(AssignmentTestCaseDto newTestCase : newTestCaseList ) {
			boolean isFound = false;
			
			for(TestCaseTblEntity nowTestCase : nowTestCaseSet ) {
				if(    newTestCase.getInput().equals(nowTestCase.getInputText()) &&
						newTestCase.getOutput().equals(nowTestCase.getOutputTxt())) {
					isFound = true;
					break;
				}
				if( !isFound ) {
					isChange = true;	//変更有！
					break;
				}
			}
		}
		return isChange;
	}
	
	/**
	 * リスト用のDTOをEntityより取得する
	 * @param assignmentTblEntity
	 * @return
	 */
	private AssignmentElementDto getFrom(Integer userId,AssignmentTblEntity assignmentTblEntity) {
		AssignmentElementDto dto = new AssignmentElementDto();
		
		setElementData(userId,assignmentTblEntity,dto);
		
		return dto;
	}
	private void setElementData(Integer userId,AssignmentTblEntity assignmentTblEntity,AssignmentElementDto dto) {

		dto.setGroupName(assignmentTblEntity.getGroupTbl().getName());
		dto.setTitle(assignmentTblEntity.getTitle());
		dto.setAssignmentId(assignmentTblEntity.getAssignmentId());
		dto.setDifficulty(assignmentTblEntity.getDifficulty());
		//解答を取得する
		AnswerTblEntity answerEntity =
				answerRepository.getOne(assignmentTblEntity.getAssignmentId(), userId);
		if( answerEntity != null ) {
			dto.setCorrect((answerEntity.getCorrectFlg()==1?true:false));
			dto.setScore(answerEntity.getScore());
			dto.setUpdateDate(answerEntity.getAnswerDate());
			dto.setPoint(answerEntity.getPoint());
			dto.setHandNum(answerEntity.getHandNum());
		}else {
			dto.setCorrect(false);
			dto.setScore(null);
			dto.setUpdateDate(null);
			dto.setPoint(0);
			dto.setHandNum(0);
		}
	}
	
	/**
	 * 課題の詳細情報を取得する
	 * 
	 * @param assignmentTblEntity
	 * @return
	 */
	private AssignmentDetailDto getDetailFrom(Integer userId,AssignmentTblEntity assignmentTblEntity) {
		AssignmentDetailDto detail = new AssignmentDetailDto();
		
		setElementData(userId,assignmentTblEntity,detail);
		detail.setContent(assignmentTblEntity.getContents());
		//解答を取得する
		AnswerTblEntity answerEntity =
				answerRepository.getOne(assignmentTblEntity.getAssignmentId(), userId);
		
		GradingResultDto gradingResult = new GradingResultDto();
		if( answerEntity!= null ) {
			//直接入力された答え（ファイル名が空）のものを抜き出す
			for( AnswerDetailTblEntity entity : answerEntity.getAnswerDetailTblSet()) {
				String fileName = entity.getFilename();
				if( fileName != null && fileName.isEmpty()) {
					detail.setAnswer(entity.getAnswer());
					break;
				}
			}
			
			for( TestCaseAnswerTblEntity testCaseAns :  answerEntity.getTestCaseAnswerTblSet() ) {
				GradingTestCaseResultDto testCaseRet = new GradingTestCaseResultDto();
				
				testCaseRet.setCorrect(testCaseAns.getCorrectly()==1);
				testCaseRet.setInput(testCaseAns.getTestCaseTbl().getInputText());
				testCaseRet.setCorrectOutput(testCaseAns.getTestCaseTbl().getOutputTxt());
				testCaseRet.setUserOutput(testCaseAns.getUserOutput());
				
				gradingResult.addGradingTestCaseResult(testCaseRet);
			}
			
			gradingResult.setScoreForOutput(answerEntity.getOutputScore());
			gradingResult.setScoreForSource(answerEntity.getSourceScore());
			gradingResult.setCheckStyleMsg(answerEntity.getCheckStyleMsg());
			gradingResult.setPoint(answerEntity.getPoint());
		}
		detail.setGradingResultDto(gradingResult);
		//スキル設定を取得する
		List<SkillDto> skillList = new ArrayList<>();
		for( SkillAssTblEntity saEntity : assignmentTblEntity.getSkillAssTblSet() ) {
			SkillDto skillDto = new SkillDto();
			skillDto.setSkillId( saEntity.getSkillTbl().getSkillId() );
			skillDto.setName( saEntity.getSkillTbl().getName() );
			skillDto.setUpdateDate( saEntity.getSkillTbl().getUpdateDate() );
			skillList.add(skillDto);
		}
		detail.setSkillList(skillList);
		
		return detail;
	}
}
