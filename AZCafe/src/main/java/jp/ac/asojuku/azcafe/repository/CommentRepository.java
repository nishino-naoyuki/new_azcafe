package jp.ac.asojuku.azcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.ac.asojuku.azcafe.entity.CommentTblEntity;


public interface CommentRepository 
	extends JpaRepository<CommentTblEntity,Integer>{

}
