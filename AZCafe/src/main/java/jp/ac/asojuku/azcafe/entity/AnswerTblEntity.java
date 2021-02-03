package jp.ac.asojuku.azcafe.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * 解答テーブル モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Data
@Entity 
@Table(name="ANSWER_TBL")
public class AnswerTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** answer_id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer answerId;

	/** 問題テーブル. */
	@OneToOne
    @JoinColumn(name="assignmentId",insertable=false ,updatable=false)
	private AssignmentTblEntity questionTbl;

	/** ユーザー. */
	@OneToOne
    @JoinColumn(name="userId",insertable=false ,updatable=false)
	private UserTblEntity userTbl;

	/** 点数. */
	private Integer score;

	/** 正解フラグ. */
	private Integer correctFlg;

	/** 解答詳細テーブル 一覧. */
	@OneToMany
	@JoinColumn(name="answerId",insertable=true ,updatable=true)
	private Set<AnswerDetailTblEntity> answerDetailTblSet;
	
	/** 解答いいね 一覧. */
	@OneToMany
	@JoinColumn(name="answerId",insertable=true ,updatable=true)
	private Set<AnswerGoodTblEntity> answerGoodTblSet;

	/** コメント 一覧. */
	@OneToMany
	@JoinColumn(name="answerId",insertable=true ,updatable=true)
	private Set<CommentTblEntity> commentTblSet;

	/**
	 * コンストラクタ.
	 */
	public AnswerTblEntity() {
		this.answerGoodTblSet = new HashSet<AnswerGoodTblEntity>();
		this.commentTblSet = new HashSet<CommentTblEntity>();
	}

}
