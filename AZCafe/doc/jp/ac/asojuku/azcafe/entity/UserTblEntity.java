package jp.ac.asojuku.azcafe.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * ユーザー モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class UserTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** user_id. */
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
	private HomeroomTblEntity homeroomTbl;

	/** 学年. */
	private Integer grade;

	/** 入学年度. */
	private Integer enterYear;

	/** 紹介文. */
	private String introduction;

	/** 称号マスターテーブル. */
	private LevelTblEntity levelTbl;

	/** 解答いいね 一覧. */
	private Set<AnswerGoodTblEntity> answerGoodTblSet;

	/** 解答テーブル 一覧. */
	private Set<AnswerTblEntity> answerTblSet;

	/** コメント 一覧. */
	private Set<CommentTblEntity> commentTblSet;

	/** フォロー 一覧. */
	private Set<FollowTblEntity> followTblSet;

	/** フォロー 一覧. */
	private Set<FollowTblEntity> followTblSet;

	/** 問題イイネテーブル 一覧. */
	private Set<QuestionGoodTblEntity> questionGoodTblSet;

	/**
	 * コンストラクタ.
	 */
	public UserTblEntity() {
		this.answerGoodTblSet = new HashSet<AnswerGoodTblEntity>();
		this.answerTblSet = new HashSet<AnswerTblEntity>();
		this.commentTblSet = new HashSet<CommentTblEntity>();
		this.followTblSet = new HashSet<FollowTblEntity>();
		this.questionGoodTblSet = new HashSet<QuestionGoodTblEntity>();
	}

	/**
	 * user_id を設定します.
	 * 
	 * @param userId
	 *            user_id
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * user_id を取得します.
	 * 
	 * @return user_id
	 */
	public Integer getUserId() {
		return this.userId;
	}

	/**
	 * 学籍番号/社員番号 を設定します.
	 * 
	 * @param orgNo
	 *            学籍番号/社員番号
	 */
	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}

	/**
	 * 学籍番号/社員番号 を取得します.
	 * 
	 * @return 学籍番号/社員番号
	 */
	public String getOrgNo() {
		return this.orgNo;
	}

	/**
	 * ニックネーム を設定します.
	 * 
	 * @param nickName
	 *            ニックネーム
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * ニックネーム を取得します.
	 * 
	 * @return ニックネーム
	 */
	public String getNickName() {
		return this.nickName;
	}

	/**
	 * 学生名 を設定します.
	 * 
	 * @param name
	 *            学生名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 学生名 を取得します.
	 * 
	 * @return 学生名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * メールアドレス を設定します.
	 * 
	 * @param mail
	 *            メールアドレス
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * メールアドレス を取得します.
	 * 
	 * @return メールアドレス
	 */
	public String getMail() {
		return this.mail;
	}

	/**
	 * ログインパスワード を設定します.
	 * 
	 * @param password
	 *            ログインパスワード
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * ログインパスワード を取得します.
	 * 
	 * @return ログインパスワード
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * ユーザーのタイプ を設定します.
	 * 
	 * @param role
	 *            ユーザーのタイプ
	 */
	public void setRole(Integer role) {
		this.role = role;
	}

	/**
	 * ユーザーのタイプ を取得します.
	 * 
	 * @return ユーザーのタイプ
	 */
	public Integer getRole() {
		return this.role;
	}

	/**
	 * 新規テーブル を設定します.
	 * 
	 * @param homeroomTbl
	 *            新規テーブル
	 */
	public void setHomeroomTbl(HomeroomTblEntity homeroomTbl) {
		this.homeroomTbl = homeroomTbl;
	}

	/**
	 * 新規テーブル を取得します.
	 * 
	 * @return 新規テーブル
	 */
	public HomeroomTblEntity getHomeroomTbl() {
		return this.homeroomTbl;
	}

	/**
	 * 学年 を設定します.
	 * 
	 * @param grade
	 *            学年
	 */
	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	/**
	 * 学年 を取得します.
	 * 
	 * @return 学年
	 */
	public Integer getGrade() {
		return this.grade;
	}

	/**
	 * 入学年度 を設定します.
	 * 
	 * @param enterYear
	 *            入学年度
	 */
	public void setEnterYear(Integer enterYear) {
		this.enterYear = enterYear;
	}

	/**
	 * 入学年度 を取得します.
	 * 
	 * @return 入学年度
	 */
	public Integer getEnterYear() {
		return this.enterYear;
	}

	/**
	 * 紹介文 を設定します.
	 * 
	 * @param introduction
	 *            紹介文
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * 紹介文 を取得します.
	 * 
	 * @return 紹介文
	 */
	public String getIntroduction() {
		return this.introduction;
	}

	/**
	 * 称号マスターテーブル を設定します.
	 * 
	 * @param levelTbl
	 *            称号マスターテーブル
	 */
	public void setLevelTbl(LevelTblEntity levelTbl) {
		this.levelTbl = levelTbl;
	}

	/**
	 * 称号マスターテーブル を取得します.
	 * 
	 * @return 称号マスターテーブル
	 */
	public LevelTblEntity getLevelTbl() {
		return this.levelTbl;
	}

	/**
	 * 解答いいね 一覧を設定します.
	 * 
	 * @param answerGoodTblSet
	 *            解答いいね 一覧
	 */
	public void setAnswerGoodTblSet(Set<AnswerGoodTblEntity> answerGoodTblSet) {
		this.answerGoodTblSet = answerGoodTblSet;
	}

	/**
	 * 解答いいね を追加します.
	 * 
	 * @param answerGoodTbl
	 *            解答いいね
	 */
	public void addAnswerGoodTbl(AnswerGoodTblEntity answerGoodTbl) {
		this.answerGoodTblSet.add(answerGoodTbl);
	}

	/**
	 * 解答いいね 一覧を取得します.
	 * 
	 * @return 解答いいね 一覧
	 */
	public Set<AnswerGoodTblEntity> getAnswerGoodTblSet() {
		return this.answerGoodTblSet;
	}

	/**
	 * 解答テーブル 一覧を設定します.
	 * 
	 * @param answerTblSet
	 *            解答テーブル 一覧
	 */
	public void setAnswerTblSet(Set<AnswerTblEntity> answerTblSet) {
		this.answerTblSet = answerTblSet;
	}

	/**
	 * 解答テーブル を追加します.
	 * 
	 * @param answerTbl
	 *            解答テーブル
	 */
	public void addAnswerTbl(AnswerTblEntity answerTbl) {
		this.answerTblSet.add(answerTbl);
	}

	/**
	 * 解答テーブル 一覧を取得します.
	 * 
	 * @return 解答テーブル 一覧
	 */
	public Set<AnswerTblEntity> getAnswerTblSet() {
		return this.answerTblSet;
	}

	/**
	 * コメント 一覧を設定します.
	 * 
	 * @param commentTblSet
	 *            コメント 一覧
	 */
	public void setCommentTblSet(Set<CommentTblEntity> commentTblSet) {
		this.commentTblSet = commentTblSet;
	}

	/**
	 * コメント を追加します.
	 * 
	 * @param commentTbl
	 *            コメント
	 */
	public void addCommentTbl(CommentTblEntity commentTbl) {
		this.commentTblSet.add(commentTbl);
	}

	/**
	 * コメント 一覧を取得します.
	 * 
	 * @return コメント 一覧
	 */
	public Set<CommentTblEntity> getCommentTblSet() {
		return this.commentTblSet;
	}

	/**
	 * フォロー 一覧を設定します.
	 * 
	 * @param followTblSet
	 *            フォロー 一覧
	 */
	public void setFollowTblSet(Set<FollowTblEntity> followTblSet) {
		this.followTblSet = followTblSet;
	}

	/**
	 * フォロー を追加します.
	 * 
	 * @param followTbl
	 *            フォロー
	 */
	public void addFollowTbl(FollowTblEntity followTbl) {
		this.followTblSet.add(followTbl);
	}

	/**
	 * フォロー 一覧を取得します.
	 * 
	 * @return フォロー 一覧
	 */
	public Set<FollowTblEntity> getFollowTblSet() {
		return this.followTblSet;
	}

	/**
	 * フォロー 一覧を設定します.
	 * 
	 * @param followTblSet
	 *            フォロー 一覧
	 */
	public void setFollowTblSet(Set<FollowTblEntity> followTblSet) {
		this.followTblSet = followTblSet;
	}

	/**
	 * フォロー を追加します.
	 * 
	 * @param followTbl
	 *            フォロー
	 */
	public void addFollowTbl(FollowTblEntity followTbl) {
		this.followTblSet.add(followTbl);
	}

	/**
	 * フォロー 一覧を取得します.
	 * 
	 * @return フォロー 一覧
	 */
	public Set<FollowTblEntity> getFollowTblSet() {
		return this.followTblSet;
	}

	/**
	 * 問題イイネテーブル 一覧を設定します.
	 * 
	 * @param questionGoodTblSet
	 *            問題イイネテーブル 一覧
	 */
	public void setQuestionGoodTblSet(Set<QuestionGoodTblEntity> questionGoodTblSet) {
		this.questionGoodTblSet = questionGoodTblSet;
	}

	/**
	 * 問題イイネテーブル を追加します.
	 * 
	 * @param questionGoodTbl
	 *            問題イイネテーブル
	 */
	public void addQuestionGoodTbl(QuestionGoodTblEntity questionGoodTbl) {
		this.questionGoodTblSet.add(questionGoodTbl);
	}

	/**
	 * 問題イイネテーブル 一覧を取得します.
	 * 
	 * @return 問題イイネテーブル 一覧
	 */
	public Set<QuestionGoodTblEntity> getQuestionGoodTblSet() {
		return this.questionGoodTblSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserTblEntity other = (UserTblEntity) obj;
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

}