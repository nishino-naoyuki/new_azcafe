package jp.ac.asojuku.azcafe.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Id;

import lombok.Data;

@Embeddable
@Data
public class FollowTblId implements Serializable{

	public FollowTblId() {}
	public FollowTblId(Integer userId,Integer follewUserId) {
		this.userId = userId;
		this.follewUserId = follewUserId;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Integer userId;	//挿入・更新用
	@Id
	private Integer follewUserId;	//挿入・更新用
}
