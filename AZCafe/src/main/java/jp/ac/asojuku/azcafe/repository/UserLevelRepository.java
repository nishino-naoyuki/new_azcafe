package jp.ac.asojuku.azcafe.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.UserLevelTblEntity;

public interface UserLevelRepository 
  extends JpaSpecificationExecutor<UserLevelTblEntity>,JpaRepository<UserLevelTblEntity,Integer>{

	@Modifying
	@Transactional
	@Query("delete from UserLevelTblEntity t where userId=:userId")
	public void delete(@Param("userId") int userId);
}
