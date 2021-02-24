package jp.ac.asojuku.azcafe.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.SkillMapTblEntity;

public interface SkillMapRepository 
		extends JpaSpecificationExecutor<SkillMapTblEntity>,JpaRepository<SkillMapTblEntity,Integer>{

	@Query("select s from SkillMapTblEntity s where skillId = :skillId and userId = :userId")
	public SkillMapTblEntity getOne(@Param("skillId")Integer skillId,@Param("userId")Integer userId);

	@Query("select s from SkillMapTblEntity s where userId = :userId")
	public List<SkillMapTblEntity> getList(@Param("userId")Integer userId);

	@Modifying
	@Transactional
	@Query("delete from SkillMapTblEntity s where userId=:userId")
	public void delete(@Param("userId") int userId);
}
