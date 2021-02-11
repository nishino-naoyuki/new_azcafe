package jp.ac.asojuku.azcafe.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.AnswerDetailTblEntity;

public interface AnswerDetailRepository 
extends JpaSpecificationExecutor<AnswerDetailTblEntity>, JpaRepository<AnswerDetailTblEntity,Integer>{
	@Modifying
	@Transactional
	@Query("delete from AnswerDetailTblEntity a where answerId=:answerId")
	public void delete(@Param("answerId") int answerId);
}
