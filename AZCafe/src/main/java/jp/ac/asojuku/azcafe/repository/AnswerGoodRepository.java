package jp.ac.asojuku.azcafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.AnswerGoodTblEntity;

public interface AnswerGoodRepository 
	extends JpaSpecificationExecutor<AnswerGoodTblEntity>, JpaRepository<AnswerGoodTblEntity,Integer>{

	@Query("select count(*) from AnswerGoodTblEntity g "
			+ ",AnswerTblEntity a "
			+ "where a.answerId=g.answerId "
			+ " and a.userId = :userId")
	public Integer getGoodCount(@Param("userId")Integer userId);

	@Query("select count(*) from AnswerGoodTblEntity g where g.answerId = :answerId")
	public Integer getGoodCountForAnswert(@Param("answerId")Integer answerId);

	@Query("select g from AnswerGoodTblEntity g "
			+ ",AnswerTblEntity a "
			+ "where a.answerId=g.answerId "
			+ "and a.userId = :userId "
			+ "and DATEDIFF(now() ,g.goodDate) <= 5")
	public List<AnswerGoodTblEntity> getGoodCountRecently(@Param("userId")Integer userId);

	@Query("select g from AnswerGoodTblEntity g "
			+ ",AnswerTblEntity a "
			+ ",FollowTblEntity f "
			+ "where a.answerId=g.answerId "
			+ "and g.userId = f.follewUserId "
			+ "and f.userId = :userId "
			+ "and a.userId <> :userId "
			+ "and DATEDIFF(now() ,g.goodDate) <= 5")
	public List<AnswerGoodTblEntity> getGoodCountByFollowRecently(@Param("userId")Integer userId);
	
	@Query("select g from AnswerGoodTblEntity g where userId = :userId and answerId = :answerId")
	public AnswerGoodTblEntity getGoodBy(@Param("answerId")Integer answerId,@Param("userId")Integer userId);
}
