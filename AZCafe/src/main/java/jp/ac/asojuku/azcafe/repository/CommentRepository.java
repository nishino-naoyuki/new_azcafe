package jp.ac.asojuku.azcafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.azcafe.entity.CommentTblEntity;


public interface CommentRepository 
	extends JpaRepository<CommentTblEntity,Integer>{

	@Query("select c from CommentTblEntity c "
			+ ", AnswerTblEntity a "
			+ "where c.answerId=a.answerId and a.userId = :userId and DATEDIFF(now() ,c.entryDate) <= 5")
	public List<CommentTblEntity> getCommentRecentry(@Param("userId")Integer userId);

	//userIdで指定した人がフォロー中の人の中でコメントを最近した人を取得する
	@Query("select c from CommentTblEntity c "
			+ ", AnswerTblEntity a "
			+ ", FollowTblEntity f "
			+ "where c.answerId=a.answerId "
			+ "and c.userId = f.follewUserId "
			+ "and f.userId = :userId "
			+ "and a.userId <> :userId "
			+ "and DATEDIFF(now() ,c.entryDate) <= 5")
	public List<CommentTblEntity> getCommentByFollowRecentry(@Param("userId")Integer userId);
}
