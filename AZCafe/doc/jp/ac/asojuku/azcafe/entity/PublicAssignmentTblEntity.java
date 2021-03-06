package jp.ac.asojuku.azcafe.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 公開設定 モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class PublicAssignmentTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** public_assignment_id. */
	private Integer publicAssignmentId;

	/** 問題テーブル. */
	private AssignmentTblEntity assignmentTbl;

	/** 新規テーブル. */
	private HomeroomTblEntity homeroomTbl;

	/** 公開設定. */
	private Integer publicState;

	/** 公開開始日時. */
	private Date publicStartDate;

	/** 公開終了日時. */
	private Date publicEndDate;

	/**
	 * コンストラクタ.
	 */
	public PublicAssignmentTblEntity() {
	}

	/**
	 * public_assignment_id を設定します.
	 * 
	 * @param publicAssignmentId
	 *            public_assignment_id
	 */
	public void setPublicAssignmentId(Integer publicAssignmentId) {
		this.publicAssignmentId = publicAssignmentId;
	}

	/**
	 * public_assignment_id を取得します.
	 * 
	 * @return public_assignment_id
	 */
	public Integer getPublicAssignmentId() {
		return this.publicAssignmentId;
	}

	/**
	 * 問題テーブル を設定します.
	 * 
	 * @param assignmentTbl
	 *            問題テーブル
	 */
	public void setAssignmentTbl(AssignmentTblEntity assignmentTbl) {
		this.assignmentTbl = assignmentTbl;
	}

	/**
	 * 問題テーブル を取得します.
	 * 
	 * @return 問題テーブル
	 */
	public AssignmentTblEntity getAssignmentTbl() {
		return this.assignmentTbl;
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
	 * 公開設定 を設定します.
	 * 
	 * @param publicState
	 *            公開設定
	 */
	public void setPublicState(Integer publicState) {
		this.publicState = publicState;
	}

	/**
	 * 公開設定 を取得します.
	 * 
	 * @return 公開設定
	 */
	public Integer getPublicState() {
		return this.publicState;
	}

	/**
	 * 公開開始日時 を設定します.
	 * 
	 * @param publicStartDate
	 *            公開開始日時
	 */
	public void setPublicStartDate(Date publicStartDate) {
		this.publicStartDate = publicStartDate;
	}

	/**
	 * 公開開始日時 を取得します.
	 * 
	 * @return 公開開始日時
	 */
	public Date getPublicStartDate() {
		return this.publicStartDate;
	}

	/**
	 * 公開終了日時 を設定します.
	 * 
	 * @param publicEndDate
	 *            公開終了日時
	 */
	public void setPublicEndDate(Date publicEndDate) {
		this.publicEndDate = publicEndDate;
	}

	/**
	 * 公開終了日時 を取得します.
	 * 
	 * @return 公開終了日時
	 */
	public Date getPublicEndDate() {
		return this.publicEndDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((publicAssignmentId == null) ? 0 : publicAssignmentId.hashCode());
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
		PublicAssignmentTblEntity other = (PublicAssignmentTblEntity) obj;
		if (publicAssignmentId == null) {
			if (other.publicAssignmentId != null) {
				return false;
			}
		} else if (!publicAssignmentId.equals(other.publicAssignmentId)) {
			return false;
		}
		return true;
	}

}
