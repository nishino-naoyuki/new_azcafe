package jp.ac.asojuku.azcafe.service;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.azcafe.dto.GradingResultDto;
import jp.ac.asojuku.azcafe.dto.GradingTestCaseResultDto;
import jp.ac.asojuku.azcafe.entity.AnswerDetailTblEntity;
import jp.ac.asojuku.azcafe.entity.AnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseAnswerTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.repository.AnswerDetailRepository;
import jp.ac.asojuku.azcafe.repository.AnswerRepository;
import jp.ac.asojuku.azcafe.repository.AssignmentRepository;
import jp.ac.asojuku.azcafe.repository.TestCaseAnswerRepository;
import jp.ac.asojuku.azcafe.service.grading.GradingFactory;
import jp.ac.asojuku.azcafe.service.grading.GradingProcess;
import jp.ac.asojuku.azcafe.util.FileUtils;

/**
 * 採点サービス
 * @author nishino
 *
 */
@Service
public class GradingService {
	
	@Autowired
	AssignmentRepository assignmentRepository;
	@Autowired
	AnswerRepository answerRepository;
	@Autowired
	TestCaseAnswerRepository testCaseAnswerRepository;
	@Autowired
	AnswerDetailRepository answerDetailRepository;

	/**
	 * @param userId
	 * @param lang
	 * @param code
	 * @throws AZCafeException
	 */
	@Transactional(rollbackFor = Exception.class)
	public GradingResultDto execByText(
			Integer userId,
			Integer assignmentId,
			Language lang,
			String code) throws AZCafeException {

		GradingProcess gp = GradingFactory.getGradingProcess(lang);
		AssignmentTblEntity entity = assignmentRepository.getOne(assignmentId);
		
		//処理を実行する
		GradingResultDto result = gp.execByText(entity,userId, code);
		
		if( StringUtils.isEmpty(result.getCompleErrMsg()) ) {
			//結果をDBに登録（コンパイル失敗は登録しない）
			AnswerTblEntity answerEntity = insertAnswerInfo(userId,assignmentId,result);		
			//いったん削除して登録しなおす
			answerDetailRepository.delete(answerEntity.getAnswerId());
			insertAnswerCode(answerEntity,"",code);
		}
		
		return result;
	}
	
	public GradingResultDto execByFile(
			Integer userId,
			Integer assignmentId,
			Language lang,
			List<File> srcFileList) throws AZCafeException {
		
		GradingProcess gp = GradingFactory.getGradingProcess(lang);
		AssignmentTblEntity entity = assignmentRepository.getOne(assignmentId);

		//処理を実行する
		GradingResultDto result = gp.execByFile(entity,userId, srcFileList);
		
		if( StringUtils.isEmpty(result.getCompleErrMsg()) ) {
			//結果をDBに登録（コンパイル失敗は登録しない）
			AnswerTblEntity answerEntity = insertAnswerInfo(userId,assignmentId,result);
			insertAnswerCodes(answerEntity,srcFileList);
		}
		
		return result;
	}

	/**
	 * @param userId
	 * @param assignmentId
	 * @param result
	 * @param code
	 */
	private AnswerTblEntity insertAnswerInfo(
			Integer userId,
			Integer assignmentId,
			GradingResultDto result) {
		
		//すでに登録されているかを取得する
		AnswerTblEntity answerEntity = answerRepository.getOne(assignmentId, userId);
		//結果をDBに登録
		if( answerEntity == null ) {
			//新規登録
			answerEntity = new AnswerTblEntity();
			answerEntity.setUserId(userId);
			answerEntity.setAssignmentId(assignmentId);
		}		
		answerEntity.setCorrectFlg((result.isCorrect() ? 1:0));
		answerEntity.setScore(result.getScoreForOutput()+result.getScoreForSource());
		answerEntity.setOutputScore(result.getScoreForOutput());
		answerEntity.setSourceScore(result.getScoreForSource());
		answerEntity.setCheckStyleMsg(result.getCheckStyleMsg());
		
		answerRepository.save(answerEntity);
		
		//テストケースごとの結果をセット
		List<TestCaseAnswerTblEntity> TestCaseAnswerTblEntityList = new ArrayList<>();
		for( GradingTestCaseResultDto testCase : result.getTestCaseResultList()) {
			
			//登録済みのものを取得する
			TestCaseAnswerTblEntity testcaseAnswerEntity = 
					testCaseAnswerRepository.getOne(testCase.getTestcaseId(), answerEntity.getAnswerId());
			if( testcaseAnswerEntity == null) {
				testcaseAnswerEntity = new TestCaseAnswerTblEntity();
				testcaseAnswerEntity.setTestcaseId(testCase.getTestcaseId());
				testcaseAnswerEntity.setAnswerId(answerEntity.getAnswerId());
			}
			
			testcaseAnswerEntity.setCorrectly(testCase.isCorrect()?1:0);
			testcaseAnswerEntity.setUserOutput(testCase.getUserOutput());
			
			TestCaseAnswerTblEntityList.add(testcaseAnswerEntity);
		}
		testCaseAnswerRepository.saveAll(TestCaseAnswerTblEntityList);
		
		return answerEntity;
	}
	
	private void insertAnswerCodes(AnswerTblEntity answerEntity,List<File> codeList) {
		//いったん削除して登録しなおす
		answerDetailRepository.delete(answerEntity.getAnswerId());
		
		for( File codeFile : codeList ) {
			String code = FileUtils.read(codeFile);
			insertAnswerCode(answerEntity,codeFile.getName(),code);
		}
	}
	
	private void insertAnswerCode(AnswerTblEntity answerEntity,String fileName,String code) {

		//ソースコード
		AnswerDetailTblEntity answerDetailEntity = new AnswerDetailTblEntity();
		
		answerDetailEntity.setAnswerId(answerEntity.getAnswerId());
		answerDetailEntity.setAnswer(code);
		answerDetailEntity.setFilename(fileName);
		
		answerDetailRepository.save(answerDetailEntity);
	}
}
