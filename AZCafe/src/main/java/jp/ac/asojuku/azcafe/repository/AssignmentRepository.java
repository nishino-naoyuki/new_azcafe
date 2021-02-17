package jp.ac.asojuku.azcafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;

public interface AssignmentRepository 
	extends JpaSpecificationExecutor<AssignmentTblEntity>, JpaRepository<AssignmentTblEntity,Integer>{

	@Query("select count(*) from AssignmentTblEntity a "
			+ "left join a.publicQuestionTblSet p "
			+ "where p.homeroomId = :homeroomId and p.publicState <> 0")
	public Integer getAssignmentCount(@Param("homeroomId")Integer homeroomId);

	@Query("select a from AssignmentTblEntity a "
			+ "left join a.publicQuestionTblSet p "
			+ "where p.homeroomId = :homeroomId and p.publicState <> 0 and DATEDIFF(now() ,a.createDate) <= 5")
	public List<AssignmentTblEntity> getRecentlyNewAssignment(@Param("homeroomId")Integer homeroomId);

	@Query("select a from AssignmentTblEntity a "
			+ ",AnswerTblEntity s "
			+ "where a.assignmentId = s.assignmentId and s.answerId = :answerId")
	public AssignmentTblEntity getAssignmentByAnsId(@Param("answerId")Integer answerId);
	
}
