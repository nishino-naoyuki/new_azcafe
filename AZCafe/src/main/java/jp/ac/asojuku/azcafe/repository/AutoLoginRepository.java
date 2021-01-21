package jp.ac.asojuku.azcafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.AutologinTblEntity;


public interface AutoLoginRepository 
extends JpaRepository<AutologinTblEntity,Integer>{

	@Query("select a from AutologinTblEntity a where a.token=:token and a.lmitDate > now()")
	public AutologinTblEntity getTokenInfo(@Param("token")String token);
	
	@Query("select a from AutologinTblEntity a where  a.lmitDate < now()")
	public List<AutologinTblEntity> getLimitTokenInfo();
	
}
