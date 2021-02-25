package jp.ac.asojuku.azcafe.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.TestCaseTblEntity;

public interface TestCaseRepository 
extends JpaSpecificationExecutor<TestCaseTblEntity>, JpaRepository<TestCaseTblEntity,Integer>{

	@Modifying
	@Transactional
	@Query("delete from TestCaseTblEntity t where assignmentId=:assignmentId")
	public void delete(@Param("assignmentId") int assignmentId);
}
