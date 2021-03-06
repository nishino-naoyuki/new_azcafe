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
public class CourseTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** course_id. */
	private Integer courseId;

	/** 学科名. */
	private String name;

	/** 年数. */
	private Integer year;

	/** 新規テーブル 一覧. */
	private Set<HomeroomTblEntity> homeroomTblSet;

	/**
	 * コンストラクタ.
	 */
	public CourseTblEntity() {
		this.homeroomTblSet = new HashSet<HomeroomTblEntity>();
	}

	/**
	 * course_id を設定します.
	 * 
	 * @param courseId
	 *            course_id
	 */
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	/**
	 * course_id を取得します.
	 * 
	 * @return course_id
	 */
	public Integer getCourseId() {
		return this.courseId;
	}

	/**
	 * 学科名 を設定します.
	 * 
	 * @param name
	 *            学科名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 学科名 を取得します.
	 * 
	 * @return 学科名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 年数 を設定します.
	 * 
	 * @param year
	 *            年数
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * 年数 を取得します.
	 * 
	 * @return 年数
	 */
	public Integer getYear() {
		return this.year;
	}

	/**
	 * 新規テーブル 一覧を設定します.
	 * 
	 * @param homeroomTblSet
	 *            新規テーブル 一覧
	 */
	public void setHomeroomTblSet(Set<HomeroomTblEntity> homeroomTblSet) {
		this.homeroomTblSet = homeroomTblSet;
	}

	/**
	 * 新規テーブル を追加します.
	 * 
	 * @param homeroomTbl
	 *            新規テーブル
	 */
	public void addHomeroomTbl(HomeroomTblEntity homeroomTbl) {
		this.homeroomTblSet.add(homeroomTbl);
	}

	/**
	 * 新規テーブル 一覧を取得します.
	 * 
	 * @return 新規テーブル 一覧
	 */
	public Set<HomeroomTblEntity> getHomeroomTblSet() {
		return this.homeroomTblSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
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
		CourseTblEntity other = (CourseTblEntity) obj;
		if (courseId == null) {
			if (other.courseId != null) {
				return false;
			}
		} else if (!courseId.equals(other.courseId)) {
			return false;
		}
		return true;
	}

}
