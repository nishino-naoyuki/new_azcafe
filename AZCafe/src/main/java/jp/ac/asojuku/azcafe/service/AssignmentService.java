package jp.ac.asojuku.azcafe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.azcafe.dto.AssignmentTestCaseDto;
import jp.ac.asojuku.azcafe.dto.GradingResultDto;
import jp.ac.asojuku.azcafe.dto.GradingTestCaseResultDto;
import jp.ac.asojuku.azcafe.dto.AssignmentDetailDto;
import jp.ac.asojuku.azcafe.dto.AssignmentDto;
import jp.ac.asojuku.azcafe.dto.AssignmentElementDto;
import jp.ac.asojuku.azcafe.dto.AssignmentPublicDto;
import jp.ac.asojuku.azcafe.entity.AnswerDetailTblEntity;
import jp.ac.asojuku.azcafe.entity.AnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.GroupTblEntity;
import jp.ac.asojuku.azcafe.entity.PublicAssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseAnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseTblEntity;
import jp.ac.asojuku.azcafe.param.Difficulty;
import jp.ac.asojuku.azcafe.repository.AnswerRepository;
import jp.ac.asojuku.azcafe.repository.AssignmentRepository;
import jp.ac.asojuku.azcafe.repository.GroupRepository;
import jp.ac.asojuku.azcafe.repository.PublicAssignmentRepository;
import jp.ac.asojuku.azcafe.repository.TestCaseRepository;

/**
 * 課題サービス
 * @author nishino
 *
 */
@Service
public class AssignmentService {

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
	
	/**
	 * 課題の詳細情報を取得する
	 * @param id
	 * @return
	 */
	public AssignmentDetailDto getDetail(Integer id,Integer userId) {
		if( id == null ) {
			return null;
		}
		AssignmentTblEntity entity = assignmentRepository.getOne(id);
		
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
	 * 課題を挿入する
	 * @param dto
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insert(AssignmentDto dto) {
		//グループ登録
		GroupTblEntity groupEntity = insertGroup(dto.getGroup());
		//本体登録
		AssignmentTblEntity assignmentTblEntity = insertAssignment(dto,groupEntity);
		//公開設定登録
		insertPublicState(dto,assignmentTblEntity);
		//テストケース登録
		insertTestCase(dto,assignmentTblEntity);
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
	private AssignmentTblEntity insertAssignment(AssignmentDto dto,GroupTblEntity groupEntity) {
		if( dto == null ) {
			return new AssignmentTblEntity();
		}
		AssignmentTblEntity entity = new AssignmentTblEntity();
		
		entity.setTitle(dto.getTitle());
		entity.setContents(dto.getContent());	
		entity.setDifficulty(dto.getDifficultyAsInt());
		entity.setGood(0);
		entity.setGroupInNo(1);//TODO:要調整
		entity.setCreateUserId(0);//TODO:要調整
		entity.setUpdateUserId(0);//TODO:要調整
		//問題グループ
		entity.setGroupId(groupEntity.getGroupId());
		
		assignmentRepository.save(entity);
		
		return entity;
	}
	
	/**
	 * 公開情報の登録
	 * 
	 * @param dto
	 * @param assignmentTblEntity
	 */
	private void insertPublicState(AssignmentDto dto,AssignmentTblEntity assignmentTblEntity) {
		List<PublicAssignmentTblEntity> publicStateList = new ArrayList<>();
		
		for( AssignmentPublicDto publicDto : dto.getPublicStateList()) {
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
		}else {
			dto.setCorrect(false);
			dto.setScore(null);
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
		}
		detail.setGradingResultDto(gradingResult);
		
		return detail;
	}
}
