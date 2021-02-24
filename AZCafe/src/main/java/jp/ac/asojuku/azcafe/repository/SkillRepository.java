package jp.ac.asojuku.azcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.azcafe.entity.SkillTblEntity;

public interface SkillRepository 
	extends JpaSpecificationExecutor<SkillTblEntity>, JpaRepository<SkillTblEntity, Integer>{

}
