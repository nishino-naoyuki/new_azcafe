package jp.ac.asojuku.azcafe.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 解答いいね モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class AnswerGoodTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** answer_good_id. */
	private Integer answerGoodId;

	/** 解答テーブル. */
	private AnswerTblEntity answerTbl;

	/** ユーザー. */
	private UserTblEntity userTbl;

	/** イイネをした日時. */
	private Date goodDate;

	/**
	 * コンストラクタ.
	 */
	public AnswerGoodTblEntity() {
	}

	/**
	 * answer_good_id を設定します.
	 * 
	 * @param answerGoodId
	 *            answer_good_id
	 */
	public void setAnswerGoodId(Integer answerGoodId) {
		this.answerGoodId = answerGoodId;
	}

	/**
	 * answer_good_id を取得します.
	 * 
	 * @return answer_good_id
	 */
	public Integer getAnswerGoodId() {
		return this.answerGoodId;
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
	 * イイネをした日時 を設定します.
	 * 
	 * @param goodDate
	 *            イイネをした日時
	 */
	public void setGoodDate(Date goodDate) {
		this.goodDate = goodDate;
	}

	/**
	 * イイネをした日時 を取得します.
	 * 
	 * @return イイネをした日時
	 */
	public Date getGoodDate() {
		return this.goodDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((answerGoodId == null) ? 0 : answerGoodId.hashCode());
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
		AnswerGoodTblEntity other = (AnswerGoodTblEntity) obj;
		if (answerGoodId == null) {
			if (other.answerGoodId != null) {
				return false;
			}
		} else if (!answerGoodId.equals(other.answerGoodId)) {
			return false;
		}
		return true;
	}

}
