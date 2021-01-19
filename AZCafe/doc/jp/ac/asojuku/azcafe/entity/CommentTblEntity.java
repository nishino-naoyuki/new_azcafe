package jp.ac.asojuku.azcafe.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * コメント モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class CommentTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 解答テーブル. */
	private AnswerTblEntity answerTbl;

	/** ユーザー. */
	private UserTblEntity userTbl;

	/** comment. */
	private String comment;

	/** entry_date. */
	private Date entryDate;

	/**
	 * コンストラクタ.
	 */
	public CommentTblEntity() {
	}

	/**
	 * 解答テーブル を設定します.
	 * 
	 * @param answerTbl
	 *            解答テーブル
	 */
	public void setAnswerTbl(AnswerTblEntity answerTbl) {
		this.answerTbl = answerTbl;
	}

	/**
	 * 解答テーブル を取得します.
	 * 
	 * @return 解答テーブル
	 */
	public AnswerTblEntity getAnswerTbl() {
		return this.answerTbl;
	}

	/**
	 * ユーザー を設定します.
	 * 
	 * @param userTbl
	 *            ユーザー
	 */
	public void setUserTbl(UserTblEntity userTbl) {
		this.userTbl = userTbl;
	}

	/**
	 * ユーザー を取得します.
	 * 
	 * @return ユーザー
	 */
	public UserTblEntity getUserTbl() {
		return this.userTbl;
	}

	/**
	 * comment を設定します.
	 * 
	 * @param comment
	 *            comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * comment を取得します.
	 * 
	 * @return comment
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * entry_date を設定します.
	 * 
	 * @param entryDate
	 *            entry_date
	 */
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	/**
	 * entry_date を取得します.
	 * 
	 * @return entry_date
	 */
	public Date getEntryDate() {
		return this.entryDate;
	}


}