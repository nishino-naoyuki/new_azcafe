package jp.ac.asojuku.azcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.azcafe.entity.TestCaseTblEntity;

public interface TestCaseRepository 
extends JpaSpecificationExecutor<TestCaseTblEntity>, JpaRepository<TestCaseTblEntity,Integer>{

}
