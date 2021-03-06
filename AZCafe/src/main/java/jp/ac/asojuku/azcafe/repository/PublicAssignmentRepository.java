package jp.ac.asojuku.azcafe.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.PublicAssignmentTblEntity;

public interface PublicAssignmentRepository 
	extends JpaSpecificationExecutor<PublicAssignmentTblEntity>, JpaRepository<PublicAssignmentTblEntity, Integer>{
	@Modifying
	@Transactional
	@Query("delete from PublicAssignmentTblEntity p where assignmentId=:assignmentId")
	public void delete(@Param("assignmentId") int assignmentId);
}
