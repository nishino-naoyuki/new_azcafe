package jp.ac.asojuku.azcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.azcafe.entity.LevelTblEntity;


public interface LevelRepository 
	extends JpaSpecificationExecutor<LevelTblEntity>,JpaRepository<LevelTblEntity,Integer>{

}
