package jp.ac.asojuku.azcafe.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.TestCaseAnswerTblEntity;


public interface TestCaseAnswerRepository 
extends JpaSpecificationExecutor<TestCaseAnswerTblEntity>, JpaRepository<TestCaseAnswerTblEntity,Integer>{

	@Query("select t from TestCaseAnswerTblEntity t where testcaseId = :testcaseId and answerId = :answerId")
	public TestCaseAnswerTblEntity getOne(@Param("testcaseId")Integer testcaseId,@Param("answerId")Integer answerId);

	@Modifying
	@Transactional
	@Query("delete  from TestCaseAnswerTblEntity t where t.answerId in (select a.answerId from AnswerTblEntity a where a.assignmentId=:assignmentId)")
	public void deleteAssignmentId(@Param("assignmentId") int assignmentId);
}
