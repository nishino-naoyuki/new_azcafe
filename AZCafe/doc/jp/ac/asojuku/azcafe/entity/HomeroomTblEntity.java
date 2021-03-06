package jp.ac.asojuku.azcafe.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 新規テーブル モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class HomeroomTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** homeroom_id. */
	private Integer homeroomId;

	/** 新規テーブル. */
	private CourseTblEntity courseTbl;

	/** クラス名. */
	private String name;

	/** 公開設定 一覧. */
	private Set<PublicAssignmentTblEntity> publicAssignmentTblSet;

	/** ユーザー 一覧. */
	private Set<UserTblEntity> userTblSet;

	/**
	 * コンストラクタ.
	 */
	public HomeroomTblEntity() {
		this.publicAssignmentTblSet = new HashSet<PublicAssignmentTblEntity>();
		this.userTblSet = new HashSet<UserTblEntity>();
	}

	/**
	 * homeroom_id を設定します.
	 * 
	 * @param homeroomId
	 *            homeroom_id
	 */
	public void setHomeroomId(Integer homeroomId) {
		this.homeroomId = homeroomId;
	}

	/**
	 * homeroom_id を取得します.
	 * 
	 * @return homeroom_id
	 */
	public Integer getHomeroomId() {
		return this.homeroomId;
	}

	/**
	 * 新規テーブル を設定します.
	 * 
	 * @param courseTbl
	 *            新規テーブル
	 */
	public void setCourseTbl(CourseTblEntity courseTbl) {
		this.courseTbl = courseTbl;
	}

	/**
	 * 新規テーブル を取得します.
	 * 
	 * @return 新規テーブル
	 */
	public CourseTblEntity getCourseTbl() {
		return this.courseTbl;
	}

	/**
	 * クラス名 を設定します.
	 * 
	 * @param name
	 *            クラス名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * クラス名 を取得します.
	 * 
	 * @return クラス名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 公開設定 一覧を設定します.
	 * 
	 * @param publicAssignmentTblSet
	 *            公開設定 一覧
	 */
	public void setPublicAssignmentTblSet(Set<PublicAssignmentTblEntity> publicAssignmentTblSet) {
		this.publicAssignmentTblSet = publicAssignmentTblSet;
	}

	/**
	 * 公開設定 を追加します.
	 * 
	 * @param publicAssignmentTbl
	 *            公開設定
	 */
	public void addPublicAssignmentTbl(PublicAssignmentTblEntity publicAssignmentTbl) {
		this.publicAssignmentTblSet.add(publicAssignmentTbl);
	}

	/**
	 * 公開設定 一覧を取得します.
	 * 
	 * @return 公開設定 一覧
	 */
	public Set<PublicAssignmentTblEntity> getPublicAssignmentTblSet() {
		return this.publicAssignmentTblSet;
	}

	/**
	 * ユーザー 一覧を設定します.
	 * 
	 * @param userTblSet
	 *            ユーザー 一覧
	 */
	public void setUserTblSet(Set<UserTblEntity> userTblSet) {
		this.userTblSet = userTblSet;
	}

	/**
	 * ユーザー を追加します.
	 * 
	 * @param userTbl
	 *            ユーザー
	 */
	public void addUserTbl(UserTblEntity userTbl) {
		this.userTblSet.add(userTbl);
	}

	/**
	 * ユーザー 一覧を取得します.
	 * 
	 * @return ユーザー 一覧
	 */
	public Set<UserTblEntity> getUserTblSet() {
		return this.userTblSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((homeroomId == null) ? 0 : homeroomId.hashCode());
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
		HomeroomTblEntity other = (HomeroomTblEntity) obj;
		if (homeroomId == null) {
			if (other.homeroomId != null) {
				return false;
			}
		} else if (!homeroomId.equals(other.homeroomId)) {
			return false;
		}
		return true;
	}

}
