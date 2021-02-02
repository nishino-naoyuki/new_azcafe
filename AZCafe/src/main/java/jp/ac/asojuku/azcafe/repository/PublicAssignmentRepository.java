package jp.ac.asojuku.azcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.azcafe.entity.PublicAssignmentTblEntity;

public interface PublicAssignmentRepository 
	extends JpaSpecificationExecutor<PublicAssignmentTblEntity>, JpaRepository<PublicAssignmentTblEntity, Integer>{

}
