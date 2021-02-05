package jp.ac.asojuku.azcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.azcafe.entity.TestCaseAnswerTblEntity;


public interface TestCaseAnswerRepository 
extends JpaSpecificationExecutor<TestCaseAnswerTblEntity>, JpaRepository<TestCaseAnswerTblEntity,Integer>{

}
