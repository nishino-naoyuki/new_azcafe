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
 * ユーザー モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Data
@Entity 
@Table(name="USER_TBL")
public class UserTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** user_id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	/** 学籍番号/社員番号. */
	private String orgNo;

	/** ニックネーム. */
	private String nickName;

	/** 学生名. */
	private String name;

	/** メールアドレス. */
	private String mail;

	/** ログインパスワード. */
	private String password;

	/** ユーザーのタイプ. */
	private Integer role;

	/** 新規テーブル. */
    @OneToOne
    @JoinColumn(name="homeroomId")
	private HomeroomTblEntity homeroomTbl;

	/** 学年. */
	private Integer grade;

	/** 入学年度. */
	private Integer enterYear;

	/** 紹介文. */
	private String introduction;

	/** アバター */
	private String avater;

	/** 点数. */
	private Integer point;

	/** フォロー数. */
	private Integer followNum;

	/** フォロワー数. */
	private Integer followerNum;

	/** イイネの数. */
	private Integer goodNum;

	/** 作成日. */
	private Date createDate;

	/** 更新日. */
	private Date updateDate;
	
	/** スキルマップ. */
	@OneToMany
	@JoinColumn(name="userId",insertable=false ,updatable=false)
	private Set<SkillMapTblEntity> skillMapTblSet;
	
	/** 称号連関. */
	@OneToMany
	@JoinColumn(name="userId",insertable=false ,updatable=false)
	private Set<UserLevelTblEntity> userLevelSet;
	
	/** 解答いいね 一覧. */
	//private Set<AnswerGoodTblEntity> answerGoodTblSet;

	/** 解答テーブル 一覧. */
	//private Set<AnswerTblEntity> answerTblSet;

	/** コメント 一覧. */
	//private Set<CommentTblEntity> commentTblSet;

	/** フォロー 一覧. */
	//private Set<FollowTblEntity> followTblSet;

	/** 問題イイネテーブル 一覧. */
	//private Set<QuestionGoodTblEntity> questionGoodTblSet;

	/**
	 * コンストラクタ.
	 */
	public UserTblEntity() {
		//this.answerGoodTblSet = new HashSet<AnswerGoodTblEntity>();
		//this.answerTblSet = new HashSet<AnswerTblEntity>();
		//this.commentTblSet = new HashSet<CommentTblEntity>();
		//this.followTblSet = new HashSet<FollowTblEntity>();
		//this.questionGoodTblSet = new HashSet<QuestionGoodTblEntity>();
		this.skillMapTblSet = new HashSet<SkillMapTblEntity>();
		this.userLevelSet = new HashSet<UserLevelTblEntity>();
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
