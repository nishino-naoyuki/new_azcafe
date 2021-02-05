package jp.ac.asojuku.azcafe.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.azcafe.dto.GradingResult;
import jp.ac.asojuku.azcafe.dto.GradingTestCaseResult;
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
	public void execByText(
			Integer userId,
			Integer assignmentId,
			Language lang,
			String code) throws AZCafeException {

		GradingProcess gp = GradingFactory.getGradingProcess(lang);
		AssignmentTblEntity entity = assignmentRepository.getOne(assignmentId);
		
		//処理を実行する
		GradingResult result = gp.execByText(entity,userId, code);
		
		//結果をDBに登録
		AnswerTblEntity answerEntity = new AnswerTblEntity();
		
		answerEntity.setUserId(userId);
		answerEntity.setAssignmentId(assignmentId);
		answerEntity.setCorrectFlg((result.isCorrect() ? 1:0));
		answerEntity.setScore(result.getScoreForOutput()+result.getScoreForSource());
		
		answerRepository.save(answerEntity);
		
		//テストケースごとの結果をセット
		List<TestCaseAnswerTblEntity> TestCaseAnswerTblEntityList = new ArrayList<>();
		for( GradingTestCaseResult testCase : result.getTestCaseResultList()) {
			TestCaseAnswerTblEntity testcaseAnswerEntity = new TestCaseAnswerTblEntity();
			
			testcaseAnswerEntity.setTestcaseId(testCase.getTestcaseId());
			testcaseAnswerEntity.setAnswerId(answerEntity.getAnswerId());
			testcaseAnswerEntity.setCorrectly(testCase.isCorrect()?1:0);
			testcaseAnswerEntity.setUserOutput(testCase.getUserOutput());
			
			TestCaseAnswerTblEntityList.add(testcaseAnswerEntity);
		}
		testCaseAnswerRepository.saveAll(TestCaseAnswerTblEntityList);
		
		//ソースコード
		AnswerDetailTblEntity answerDetailEntity = new AnswerDetailTblEntity();
		
		answerDetailEntity.setAnswerId(answerEntity.getAnswerId());
		answerDetailEntity.setAnswer(code);
		answerDetailEntity.setFilename("");
		
		answerDetailRepository.save(answerDetailEntity);
	}
	
}
