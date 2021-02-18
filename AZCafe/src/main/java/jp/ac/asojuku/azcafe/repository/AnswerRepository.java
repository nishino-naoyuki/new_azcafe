package jp.ac.asojuku.azcafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.AnswerTblEntity;

public interface AnswerRepository 
	extends JpaSpecificationExecutor<AnswerTblEntity>, JpaRepository<AnswerTblEntity,Integer>{

	@Query("select a from AnswerTblEntity a where assignmentId = :assignmentId and userId = :userId")
	public AnswerTblEntity getOne(@Param("assignmentId")Integer assignmentId,@Param("userId")Integer userId);

	@Query("select a from AnswerTblEntity a where userId = :userId")
	public List<AnswerTblEntity> getList(@Param("userId")Integer userId);

	@Query("select a from AnswerTblEntity a where assignmentId = :assignmentId")
	public List<AnswerTblEntity> getListByAssId(@Param("assignmentId")Integer assignmentId);
	
	
}
