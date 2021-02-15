package jp.ac.asojuku.azcafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.FollowTblEntity;
import jp.ac.asojuku.azcafe.entity.FollowTblId;

public interface FollowRepository 
extends JpaRepository<FollowTblEntity,FollowTblId>{

	@Query("select f from FollowTblEntity f where userId = :userId")
	public List<FollowTblEntity> getFollowUser(@Param("userId")Integer userId);

	@Query("select f from FollowTblEntity f where follewUserId = :userId")
	public List<FollowTblEntity> getFollowerUser(@Param("userId")Integer userId);
	
	@Query("select count(*) from FollowTblEntity f where userId = :userId")
	public Integer getFollowCount(@Param("userId")Integer userId);

	@Query("select count(*) from FollowTblEntity f where follewUserId = :userId")
	public Integer getFollowerCount(@Param("userId")Integer userId);
}
