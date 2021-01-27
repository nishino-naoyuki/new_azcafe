package jp.ac.asojuku.azcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.UserTblEntity;

public interface UserRepository extends JpaSpecificationExecutor<UserTblEntity>, JpaRepository<UserTblEntity, Integer> {

	@Query("select u from UserTblEntity u where mail = :mail and password = :password")
	public UserTblEntity getUser(@Param("mail")String mail,@Param("password")String password);
	
	@Query("select u from UserTblEntity u where mail = :mail")
	public UserTblEntity getUserByMail(@Param("mail")String mail);

	@Query("select u from UserTblEntity u where orgNo = :studentno")
	public UserTblEntity getUserByStudentNo(@Param("studentno")String studentno);
}
