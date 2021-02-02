package jp.ac.asojuku.azcafe.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 問題テーブル モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class AssignmentTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** assignment_id. */
	private Integer assignmentId;

	/** 問題グループ. */
	private GroupTblEntity groupTbl;

	/** グループ内の番号. */
	private Integer groupInNo;

	/** タイトル. */
	private String title;

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
	private Set<AnswerTblEntity> answerTblSet;

	/** 問題イイネテーブル 一覧. */
	private Set<AssignmentGoodTblEntity> assignmentGoodTblSet;

	/** 公開設定 一覧. */
	private Set<PublicAssignmentTblEntity> publicAssignmentTblSet;

	/** テストケース 一覧. */
	private Set<TestCaseTblEntity> testCaseTblSet;

	/**
	 * コンストラクタ.
	 */
	public AssignmentTblEntity() {
		this.answerTblSet = new HashSet<AnswerTblEntity>();
		this.assignmentGoodTblSet = new HashSet<AssignmentGoodTblEntity>();
		this.publicAssignmentTblSet = new HashSet<PublicAssignmentTblEntity>();
		this.testCaseTblSet = new HashSet<TestCaseTblEntity>();
	}

	/**
	 * assignment_id を設定します.
	 * 
	 * @param assignmentId
	 *            assignment_id
	 */
	public void setAssignmentId(Integer assignmentId) {
		this.assignmentId = assignmentId;
	}

	/**
	 * assignment_id を取得します.
	 * 
	 * @return assignment_id
	 */
	public Integer getAssignmentId() {
		return this.assignmentId;
	}

	/**
	 * 問題グループ を設定します.
	 * 
	 * @param groupTbl
	 *            問題グループ
	 */
	public void setGroupTbl(GroupTblEntity groupTbl) {
		this.groupTbl = groupTbl;
	}

	/**
	 * 問題グループ を取得します.
	 * 
	 * @return 問題グループ
	 */
	public GroupTblEntity getGroupTbl() {
		return this.groupTbl;
	}

	/**
	 * グループ内の番号 を設定します.
	 * 
	 * @param groupInNo
	 *            グループ内の番号
	 */
	public void setGroupInNo(Integer groupInNo) {
		this.groupInNo = groupInNo;
	}

	/**
	 * グループ内の番号 を取得します.
	 * 
	 * @return グループ内の番号
	 */
	public Integer getGroupInNo() {
		return this.groupInNo;
	}

	/**
	 * タイトル を設定します.
	 * 
	 * @param title
	 *            タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * タイトル を取得します.
	 * 
	 * @return タイトル
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * 内容 を設定します.
	 * 
	 * @param contents
	 *            内容
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * 内容 を取得します.
	 * 
	 * @return 内容
	 */
	public String getContents() {
		return this.contents;
	}

	/**
	 * イイネの数 を設定します.
	 * 
	 * @param good
	 *            イイネの数
	 */
	public void setGood(Integer good) {
		this.good = good;
	}

	/**
	 * イイネの数 を取得します.
	 * 
	 * @return イイネの数
	 */
	public Integer getGood() {
		return this.good;
	}

	/**
	 * 作成者 を設定します.
	 * 
	 * @param createUserId
	 *            作成者
	 */
	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * 作成者 を取得します.
	 * 
	 * @return 作成者
	 */
	public Integer getCreateUserId() {
		return this.createUserId;
	}

	/**
	 * 作成日時 を設定します.
	 * 
	 * @param createDate
	 *            作成日時
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 作成日時 を取得します.
	 * 
	 * @return 作成日時
	 */
	public Date getCreateDate() {
		return this.createDate;
	}

	/**
	 * 更新者 を設定します.
	 * 
	 * @param updateUserId
	 *            更新者
	 */
	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	/**
	 * 更新者 を取得します.
	 * 
	 * @return 更新者
	 */
	public Integer getUpdateUserId() {
		return this.updateUserId;
	}

	/**
	 * 更新日時 を設定します.
	 * 
	 * @param updateDate
	 *            更新日時
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 更新日時 を取得します.
	 * 
	 * @return 更新日時
	 */
	public Date getUpdateDate() {
		return this.updateDate;
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
	 * 問題イイネテーブル 一覧を設定します.
	 * 
	 * @param assignmentGoodTblSet
	 *            問題イイネテーブル 一覧
	 */
	public void setAssignmentGoodTblSet(Set<AssignmentGoodTblEntity> assignmentGoodTblSet) {
		this.assignmentGoodTblSet = assignmentGoodTblSet;
	}

	/**
	 * 問題イイネテーブル を追加します.
	 * 
	 * @param assignmentGoodTbl
	 *            問題イイネテーブル
	 */
	public void addAssignmentGoodTbl(AssignmentGoodTblEntity assignmentGoodTbl) {
		this.assignmentGoodTblSet.add(assignmentGoodTbl);
	}

	/**
	 * 問題イイネテーブル 一覧を取得します.
	 * 
	 * @return 問題イイネテーブル 一覧
	 */
	public Set<AssignmentGoodTblEntity> getAssignmentGoodTblSet() {
		return this.assignmentGoodTblSet;
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
	 * テストケース 一覧を設定します.
	 * 
	 * @param testCaseTblSet
	 *            テストケース 一覧
	 */
	public void setTestCaseTblSet(Set<TestCaseTblEntity> testCaseTblSet) {
		this.testCaseTblSet = testCaseTblSet;
	}

	/**
	 * テストケース を追加します.
	 * 
	 * @param testCaseTbl
	 *            テストケース
	 */
	public void addTestCaseTbl(TestCaseTblEntity testCaseTbl) {
		this.testCaseTblSet.add(testCaseTbl);
	}

	/**
	 * テストケース 一覧を取得します.
	 * 
	 * @return テストケース 一覧
	 */
	public Set<TestCaseTblEntity> getTestCaseTblSet() {
		return this.testCaseTblSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignmentId == null) ? 0 : assignmentId.hashCode());
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
		AssignmentTblEntity other = (AssignmentTblEntity) obj;
		if (assignmentId == null) {
			if (other.assignmentId != null) {
				return false;
			}
		} else if (!assignmentId.equals(other.assignmentId)) {
			return false;
		}
		return true;
	}

}
