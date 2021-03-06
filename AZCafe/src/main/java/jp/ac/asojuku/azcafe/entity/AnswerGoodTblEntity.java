package jp.ac.asojuku.azcafe.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;


import lombok.Data;

/**
 * 解答いいね モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Data
@Entity 
@Table(name="ANSWER_GOOD_TBL")
public class AnswerGoodTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer answerGoodId;
	
	private Integer answerId;	//挿入・更新用
	private Integer userId;	//挿入・更新用
	
	/** 解答テーブル. */
	//@OneToOne
    //@JoinColumn(name="answerId",insertable=false ,updatable=false)
	//private AnswerTblEntity answerTbl;

	/** ユーザー. */
	@OneToOne
    @JoinColumn(name="userId",insertable=false ,updatable=false)
	private UserTblEntity userTbl;

	/** イイネをした日時. */
	private Date goodDate;

	/**
	 * コンストラクタ.
	 */
	public AnswerGoodTblEntity() {
	}

	@PrePersist
    public void onPrePersist() {
		setGoodDate(new Date());
    }

    @PreUpdate
    public void onPreUpdate() {
    	setGoodDate(new Date());
    }

}
