package jp.ac.asojuku.azcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.AutologinTblEntity;
import jp.ac.asojuku.azcafe.entity.GroupTblEntity;

public interface GroupRepository 
extends JpaSpecificationExecutor<GroupTblEntity>, JpaRepository<GroupTblEntity,Integer>{

	@Query("select g from GroupTblEntity g where g.name=:name")
	public GroupTblEntity getGroupInfoBy(@Param("name")String name);
}
