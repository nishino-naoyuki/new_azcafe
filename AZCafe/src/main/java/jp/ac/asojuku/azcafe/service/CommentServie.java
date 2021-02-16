package jp.ac.asojuku.azcafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.azcafe.dto.CommentInsertDto;
import jp.ac.asojuku.azcafe.dto.GradingResultDetailDto;
import jp.ac.asojuku.azcafe.entity.AnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.CommentTblEntity;
import jp.ac.asojuku.azcafe.repository.AnswerRepository;
import jp.ac.asojuku.azcafe.repository.CommentRepository;

@Service
public class CommentServie {
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	AnswerRepository answerRepository;

	@Autowired
	AnswerService answerService;
	
	/**
	 * コメントを挿入する
	 * @param commentInsertDto
	 */
	public GradingResultDetailDto insert(CommentInsertDto commentInsertDto) {
		CommentTblEntity entity = new CommentTblEntity();
		
		entity.setAnswerId(commentInsertDto.getAnswerId());
		entity.setComment(commentInsertDto.getMessage());
		entity.setUserId(commentInsertDto.getUserId());
		
		//保存
		commentRepository.saveAndFlush(entity);
		
		return answerService.getBy(commentInsertDto.getAnswerUserId(), commentInsertDto.getAssignmentId());
	}
}
