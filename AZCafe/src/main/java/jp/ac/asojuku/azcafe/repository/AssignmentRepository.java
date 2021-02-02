package jp.ac.asojuku.azcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;

public interface AssignmentRepository 
	extends JpaSpecificationExecutor<AssignmentTblEntity>, JpaRepository<AssignmentTblEntity,Integer>{

}
