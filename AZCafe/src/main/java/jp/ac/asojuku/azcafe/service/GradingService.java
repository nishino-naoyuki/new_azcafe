package jp.ac.asojuku.azcafe.service;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.dto.GradingResultDto;
import jp.ac.asojuku.azcafe.dto.GradingTestCaseResultDto;
import jp.ac.asojuku.azcafe.entity.AnswerDetailTblEntity;
import jp.ac.asojuku.azcafe.entity.AnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.SkillAssTblEntity;
import jp.ac.asojuku.azcafe.entity.SkillMapTblEntity;
import jp.ac.asojuku.azcafe.entity.TestCaseAnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.UserTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Difficulty;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.repository.AnswerDetailRepository;
import jp.ac.asojuku.azcafe.repository.AnswerRepository;
import jp.ac.asojuku.azcafe.repository.AssignmentRepository;
import jp.ac.asojuku.azcafe.repository.SkillMapRepository;
import jp.ac.asojuku.azcafe.repository.TestCaseAnswerRepository;
import jp.ac.asojuku.azcafe.repository.UserRepository;
import jp.ac.asojuku.azcafe.service.grading.GradingFactory;
import jp.ac.asojuku.azcafe.service.grading.GradingJava;
import jp.ac.asojuku.azcafe.service.grading.GradingProcess;
import jp.ac.asojuku.azcafe.util.FileUtils;

/**
 * 採点サービス
 * @author nishino
 *
 */
@Service
public class GradingService {
	private static final Logger logger = LoggerFactory.getLogger(GradingService.class);
	@Autowired
	AssignmentRepository assignmentRepository;
	@Autowired
	AnswerRepository answerRepository;
	@Autowired
	TestCaseAnswerRepository testCaseAnswerRepository;
	@Autowired
	AnswerDetailRepository answerDetailRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	SkillMapRepository skillMapRepository;

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
			insertAnswerCode(answerEntity,"",code);
			//ユーザーテーブルのポイント更新
			updateUserTotalPoint(userId);
			//スキルポイントを更新
			updateSkillMap(userId,
					entity,
					(answerEntity.getCorrectFlg()==1?true:false));
		}
		
		return result;
	}
	
	@Transactional(rollbackFor = Exception.class)
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
			//ユーザーテーブルのポイント更新
			updateUserTotalPoint(userId);
			//スキルポイントを更新
			updateSkillMap(userId,
					entity,
					(answerEntity.getCorrectFlg()==1?true:false));
		}
		
		return result;
	}
	
	/**
	 * ポイントを更新する
	 * @param userId
	 */
	public void updateUserTotalPoint(Integer userId) {
		List<AnswerTblEntity> ansList = answerRepository.getList(userId);
		UserTblEntity userEntity = userRepository.getOne(userId);
		
		if( userEntity == null ) {
			return;	//念のため
		}
		
		int point = 0;
		for( AnswerTblEntity entity : ansList ) {
			point += entity.getPoint();
		}
		userEntity.setPoint(point);
		
		userRepository.save(userEntity);
	}
	
	/**
	 * スキルマップを更新する
	 * 同じ問題に対して何度も回答することと問題が更新されることを考慮して毎回作り直す
	 * 
	 * @param userId
	 * @param entity
	 * @param isCorrect
	 */
	private void updateSkillMap(Integer userId,AssignmentTblEntity entity,boolean isCorrect) {
		if( isCorrect != true || entity.getSkillAssTblSet() == null) {
			return;	//正解ではない場合何もしない
		}
		
		//ユーザーの解答をすべて取得
		List<AnswerTblEntity> ansList = answerRepository.getList(userId);
		
		//一旦ユーザーのスキルマップを全部削除する
		skillMapRepository.delete(userId);
		
		//一旦マップを作る（毎回SQLを発行すると重い）
		HashMap<Integer,Integer> skillMap = new HashMap<>();
		for(AnswerTblEntity ansEntity : ansList) {
			AssignmentTblEntity assEntity = ansEntity.getAssignmentTbl();
			if( assEntity == null && (ansEntity.getAssignmentId() == entity.getAssignmentId())) {
				//追加したばかりのAnswerTblEntityからはAssignmentTblEntityをとれない（苦肉の策・・・）
				assEntity = entity;
			}
			if( assEntity.getSkillAssTblSet() == null ) {
				continue;
			}
			
			for( SkillAssTblEntity skillass : assEntity.getSkillAssTblSet() ) {
				int skillId = skillass.getSkillId();
				if( skillMap.containsKey(skillId) ) {
					//すでに登録してる
					skillMap.put(skillId, skillMap.get(skillId)+1);
				}else {
					skillMap.put(skillId, 1);
				}				
			}
		}
		
		//マップを元に登録するEntityのリストを作る
		List<SkillMapTblEntity> entityList = new ArrayList<>();
		for(Integer key : skillMap.keySet()) {
			SkillMapTblEntity smEntity = new SkillMapTblEntity();
			smEntity.setPoint(skillMap.get(key));
			smEntity.setUserId(userId);
			smEntity.setSkillId(key);
			entityList.add(smEntity);
		}
		
		skillMapRepository.saveAll(entityList);
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
			answerEntity.setHandNum(0);
			answerEntity.setPoint(0); //ポイント初期化
		}
		answerEntity.setCorrectFlg((result.isCorrect() ? 1:0));
		answerEntity.setScore(result.getScoreForOutput()+result.getScoreForSource());
		answerEntity.setOutputScore(result.getScoreForOutput());
		answerEntity.setSourceScore(result.getScoreForSource());
		answerEntity.setCheckStyleMsg(result.getCheckStyleMsg());
		if( result.isCorrect() ) {
			//今回正解（再提出）の場合は提出回数を増やす
			answerEntity.setHandNum(answerEntity.getHandNum()+1);
		}
		//ポイントを更新する
		result.setPoint( calPoint(answerEntity,result.isCorrect()) );
		
		answerEntity = answerRepository.save(answerEntity);
		
		//logger.info("[debug]AnswerTblEntity["+assignmentId+"]["+userId+"]["+answerEntity.getAnswerId()+"]");
		
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

			//logger.info("[debug]GradingTestCaseResultDto["+testcaseAnswerEntity.getAnswerId()+"]["+testcaseAnswerEntity.getTestcaseId()+"]");
			TestCaseAnswerTblEntityList.add(testcaseAnswerEntity);
		}
		testCaseAnswerRepository.saveAll(TestCaseAnswerTblEntityList);
		
		return answerEntity;
	}
	
	/**
	 * ポイントの計算をする
	 * ※すでにAnswerEntityが作成済であることが前提
	 * 
	 * @param answerEntity
	 * @return
	 */
	public int calPoint(AnswerTblEntity answerEntity,boolean correctFlg) {
		AssignmentTblEntity assignmentEntity =answerEntity.getAssignmentTbl();
		if( assignmentEntity == null ) {
			assignmentEntity = assignmentRepository.getOne(answerEntity.getAssignmentId());
		}

		int point = answerEntity.getScore();		//基礎点
		
		if( correctFlg ) {
			//難易度ボーナスは世界の時だけ
			point += Difficulty.getBy(assignmentEntity.getDifficulty()).getPoint();
		}
		
		//提出回数原点（2回名以降減点）
		int handMinus = AZCafeConfig.getInstance().getHandminus();//1回提出当たりのマイナス
		int handNum = answerEntity.getHandNum();
		if( handNum > 1) {
			handNum--;
			point -= (handMinus*handNum);
		}
		
		answerEntity.setPoint(point);
		
		return point;
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
		//いったん削除して登録しなおす
		answerDetailRepository.delete(answerEntity.getAnswerId());

		//ソースコード
		AnswerDetailTblEntity answerDetailEntity = new AnswerDetailTblEntity();
		
		answerDetailEntity.setAnswerId(answerEntity.getAnswerId());
		answerDetailEntity.setAnswer(code);
		answerDetailEntity.setFilename(fileName);
		
		answerDetailRepository.save(answerDetailEntity);
	}
}
