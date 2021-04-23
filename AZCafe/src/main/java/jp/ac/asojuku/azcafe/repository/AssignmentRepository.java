package jp.ac.asojuku.azcafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;

public interface AssignmentRepository 
	extends JpaSpecificationExecutor<AssignmentTblEntity>, JpaRepository<AssignmentTblEntity,Integer>{

	@Query("select distinct a from AssignmentTblEntity a "
			+ "left join a.publicQuestionTblSet p "
			+ ", UserTblEntity u  "
			+ "where p.assignmentId = a.assignmentId and "
			+ "p.homeroomId = u.homeroomTbl.homeroomId and "
			+ "a.assignmentId = :assignmentId and "
			+ "((u.role=1 and u.userId = :userId) or (u.role=0 and u.userId = :userId and p.publicState <> 0))  ")
	public AssignmentTblEntity getAssignment(@Param("assignmentId")Integer assignmentId,@Param("userId")Integer userId);
	
	@Query("select distinct a from AssignmentTblEntity a "
			+ "left join a.publicQuestionTblSet p "
			+ ", UserTblEntity u  "
			+ "where p.assignmentId = a.assignmentId and "
			+ "p.homeroomId = u.homeroomTbl.homeroomId and "
			+ "((u.role=1 and u.userId = :userId) or (u.role=0 and u.userId = :userId and p.publicState <> 0))  "
			+ "order by a.groupTbl.name,a.title,a.groupInNo")
	public List<AssignmentTblEntity> getAssignmentList(@Param("userId")Integer userId);
	
	@Query("select count(distinct a) from AssignmentTblEntity a "
			+ "left join a.publicQuestionTblSet p "
			+ ", UserTblEntity u  "
			+ "where p.homeroomId = :homeroomId and p.publicState <> 0")
	public Integer getAssignmentCount(@Param("homeroomId")Integer homeroomId);

	@Query("select count(distinct a) from AssignmentTblEntity a "
			+ "left join a.publicQuestionTblSet p "
			+ ", UserTblEntity u  "
			+ "where p.homeroomId = :homeroomId")
	public Integer getAssignmentCountTeacher(@Param("homeroomId")Integer homeroomId);
	
	@Query("select a from AssignmentTblEntity a "
			+ "left join a.publicQuestionTblSet p "
			+ "where p.homeroomId = :homeroomId and p.publicState <> 0 and DATEDIFF(now() ,a.createDate) <= 5")
	public List<AssignmentTblEntity> getRecentlyNewAssignment(@Param("homeroomId")Integer homeroomId);

	@Query("select a from AssignmentTblEntity a "
			+ ",AnswerTblEntity s "
			+ "where a.assignmentId = s.assignmentId and s.answerId = :answerId")
	public AssignmentTblEntity getAssignmentByAnsId(@Param("answerId")Integer answerId);

	@Query("select a from AssignmentTblEntity a "
			+ "where a.title = :title")
	public AssignmentTblEntity getAssignmentByName(@Param("title")String title);
	
}
