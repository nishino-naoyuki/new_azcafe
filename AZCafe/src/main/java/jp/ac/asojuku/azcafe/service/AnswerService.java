package jp.ac.asojuku.azcafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.azcafe.dto.CommentDto;
import jp.ac.asojuku.azcafe.dto.GradingResultDetailDto;
import jp.ac.asojuku.azcafe.dto.GradingResultDto;
import jp.ac.asojuku.azcafe.dto.GradingTestCaseResultDto;
import jp.ac.asojuku.azcafe.dto.SourceCodeDto;
import jp.ac.asojuku.azcafe.entity.AnswerDetailTblEntity;
import jp.ac.asojuku.azcafe.entity.AnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.CommentTblEntity;
import jp.ac.asojuku.azcafe.entity.FollowTblEntity;
import jp.ac.asojuku.azcafe.entity.FollowTblId;
import jp.ac.asojuku.azcafe.entity.TestCaseAnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.UserTblEntity;
import jp.ac.asojuku.azcafe.repository.AnswerRepository;
import jp.ac.asojuku.azcafe.repository.FollowRepository;
import jp.ac.asojuku.azcafe.repository.UserRepository;

@Service
public class AnswerService {
	@Autowired
	AnswerRepository answerRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	FollowRepository followRepository;

	/**
	 * userIdで指定したユーザーのassignmentIdで指定した問題の解答を見れるかどうか
	 * ログインユーザーが見れるかどうか
	 * 
	 * @param assignmentId
	 * @param userId
	 * @param loginUserId
	 * @return
	 */
	public boolean isWatch(Integer assignmentId,Integer userId,Integer loginUserId) {
		if( userId == loginUserId) {
			//ログインユーザーが自分の情報を見るのはOK
			return true;
		}
		//ログインユーザーがその問題を提出済みであること
		AnswerTblEntity answerEntity = answerRepository.getOne(assignmentId, loginUserId);
		if( answerEntity == null ) {
			//ログインユーザーが提出済みでないなら見れない
			return false;
		}
		//ログインユーザーがそのユーザーをフォローしていること
		FollowTblEntity fEntity = followRepository.findById(new FollowTblId(loginUserId,userId)).orElse(null);
		if( fEntity == null ) {
			//フォローしていなければ見れない
			return false;
		}
		
		return true;
	}
	/**
	 * 採点結果の詳細を取得する
	 * 
	 * @param userId
	 * @param assignmentId
	 * @return
	 */
	public GradingResultDetailDto getBy(Integer userId,Integer assignmentId) {
		AnswerTblEntity answerEntity =  answerRepository.getOne(assignmentId, userId);
		
		return getDto(answerEntity);
	}
	
	/**
	 * 採点結果を取得する
	 * 
	 * @param answerEntity
	 * @return
	 */
	private GradingResultDetailDto getDto(AnswerTblEntity answerEntity) {
		GradingResultDetailDto dto = new GradingResultDetailDto();
		
		dto.setAnsUserId(answerEntity.getUserTbl().getUserId());
		dto.setAssignmentId(answerEntity.getAssignmentId());
		dto.setAnswerId(answerEntity.getAnswerId());
		dto.setTitle(answerEntity.getAssignmentTbl().getTitle()); 
		//結果の基礎情報をセット
		dto.setGradingResultDto(getGradingResultDtoFrom( answerEntity ) ); 
		//イイネの数
		if( answerEntity.getAnswerGoodTblSet() != null ) {
			dto.setGoodCount(answerEntity.getAnswerGoodTblSet().size());
		}else {
			dto.setGoodCount(0);
		}
		//コメントをセット
		if( answerEntity.getCommentTblSet() != null ) {
			for( CommentTblEntity commentEntity : answerEntity.getCommentTblSet() ) {
				dto.addComment( getCommentDtoFrom(commentEntity) );
			}
		}
		//ソースコード
		if( answerEntity.getAnswerDetailTblSet() != null ) {
			for( AnswerDetailTblEntity answerDetail : answerEntity.getAnswerDetailTblSet()) {
				dto.addSourceCode(getSourceCodeDtoFrom(answerDetail));
			}
		}
		
		return dto;
	}
	
	/**
	 * ソースコードを取得する
	 * 
	 * @param answerDetail
	 * @return
	 */
	private SourceCodeDto getSourceCodeDtoFrom(AnswerDetailTblEntity answerDetail) {
		SourceCodeDto dto = new SourceCodeDto();
		
		dto.setFileName(answerDetail.getFilename());
		dto.setCode(answerDetail.getAnswer());
		
		return dto;
	}
	
	/**
	 * コメントを取得
	 * 
	 * @param commentEntity
	 * @return
	 */
	private CommentDto getCommentDtoFrom(CommentTblEntity commentEntity) {
		CommentDto dto = new CommentDto();
		
		UserTblEntity userEnitty = commentEntity.getUserTbl();
		//挿入直後はUserEnityがnullになるので取得する
		if( userEnitty == null ) {
			userEnitty = userRepository.getOne(commentEntity.getUserId());
		}
		dto.setUserId(commentEntity.getUserId());
		dto.setUserName(userEnitty.getName());
		dto.setAvater(userEnitty.getAvater());
		dto.setMessage(commentEntity.getComment());
		dto.setCommentDate(commentEntity.getEntryDate());
		
		return dto;
	}
	
	/**
	 * 結果を取得する
	 * @param answerEntity
	 * @return
	 */
	private GradingResultDto getGradingResultDtoFrom(AnswerTblEntity answerEntity) {
		GradingResultDto gradingResultDto = new GradingResultDto();

		gradingResultDto.setCorrect(answerEntity.getCorrectFlg()==1);
		gradingResultDto.setCheckStyleMsg(answerEntity.getCheckStyleMsg());
		gradingResultDto.setScoreForOutput(answerEntity.getOutputScore());
		gradingResultDto.setScoreForSource(answerEntity.getSourceScore());
		if( answerEntity.getTestCaseAnswerTblSet() != null ) {
			//テストケース毎の結果をセットする
			for( TestCaseAnswerTblEntity testCaseEntity: answerEntity.getTestCaseAnswerTblSet()) {
				GradingTestCaseResultDto testCaseResult = new GradingTestCaseResultDto();
				
				testCaseResult.setCorrect( testCaseEntity.getCorrectly()==1 );
				testCaseResult.setInput(testCaseEntity.getTestCaseTbl().getInputText());
				testCaseResult.setCorrectOutput(testCaseEntity.getTestCaseTbl().getOutputTxt());
				testCaseResult.setUserOutput(testCaseEntity.getUserOutput());
				
				gradingResultDto.addGradingTestCaseResult(testCaseResult);
			}
		}
		
		return gradingResultDto;
	}
}
