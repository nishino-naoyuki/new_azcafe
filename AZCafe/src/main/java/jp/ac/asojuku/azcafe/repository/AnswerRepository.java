package jp.ac.asojuku.azcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.azcafe.entity.AnswerTblEntity;

public interface AnswerRepository 
	extends JpaSpecificationExecutor<AnswerTblEntity>, JpaRepository<AnswerTblEntity,Integer>{

}
