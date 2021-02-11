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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

/**
 * 問題テーブル モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Data
@Entity 
@Table(name="ASSIGNMENT_TBL")
public class AssignmentTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** assignment_id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer assignmentId;

	/** 問題グループ. */
	private Integer groupId;
	@OneToOne
    @JoinColumn(name="groupId",insertable=false ,updatable=false)
	private GroupTblEntity groupTbl;

	/** グループ内の番号. */
	private Integer groupInNo;

	/** タイトル. */
	private String title;
	
	/** 難易度. */
	private Integer difficulty;

	/** 内容. */
	private String contents;

	/** イイネの数. */
	private Integer good;

	/** 作成者. */
	private Integer createUserId;

	/** 作成日時. */
	private Date createDate;

	/** 更新者. */
	private Integer updateUserId;

	/** 更新日時. */
	private Date updateDate;

	/** 解答テーブル 一覧. */
	//@OneToOne
    //@JoinColumn(name="assignmentId",insertable=false ,updatable=false)
	//private AnswerTblEntity answerTbl;

	/** 問題イイネテーブル 一覧. */
	@OneToMany
	@JoinColumn(name="assignmentId",insertable=false ,updatable=false)
	private Set<AssignmentGoodTblEntity> assignmentGoodTblSet;

	/** 公開設定 一覧. */
	@OneToMany
	@JoinColumn(name="assignmentId",insertable=false ,updatable=false)
	private Set<PublicAssignmentTblEntity> publicQuestionTblSet;

	/** テストケース 一覧. */
	@OneToMany
	@JoinColumn(name="assignmentId",insertable=false ,updatable=false)
	private Set<TestCaseTblEntity> testCaseTblSet;

	/**
	 * コンストラクタ.
	 */
	public AssignmentTblEntity() {
		this.assignmentGoodTblSet = new HashSet<AssignmentGoodTblEntity>();
		this.publicQuestionTblSet = new HashSet<PublicAssignmentTblEntity>();
		this.testCaseTblSet = new HashSet<TestCaseTblEntity>();
	}

	@PrePersist
    public void onPrePersist() {
		setCreateDate(new Date());
		setUpdateDate(new Date());
    }

    @PreUpdate
    public void onPreUpdate() {
    	setUpdateDate(new Date());
    }
}
