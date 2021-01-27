package jp.ac.asojuku.azcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.HomeroomTblEntity;
import jp.ac.asojuku.azcafe.entity.UserTblEntity;

public interface HomeroomRepository extends JpaSpecificationExecutor<HomeroomTblEntity>, JpaRepository<HomeroomTblEntity, Integer> {

}
