package jp.ac.asojuku.azcafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.AnswerGoodTblEntity;

public interface AnswerGoodRepository 
	extends JpaSpecificationExecutor<AnswerGoodTblEntity>, JpaRepository<AnswerGoodTblEntity,Integer>{

	@Query("select count(*) from AnswerGoodTblEntity g where g.answerTbl.userId = :userId")
	public Integer getGoodCount(@Param("userId")Integer userId);

	@Query("select g from AnswerGoodTblEntity g where g.answerTbl.userId = :userId and DATEDIFF(now() ,goodDate) <= 5")
	public List<AnswerGoodTblEntity> getRecentlyGoodCount(@Param("userId")Integer userId);
}
