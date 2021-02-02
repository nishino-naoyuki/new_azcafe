package jp.ac.asojuku.azcafe.entity;

import java.io.Serializable;

/**
 * テストケース モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class TestCaseTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** testcase_id. */
	private Integer testcaseId;

	/** 問題テーブル. */
	private AssignmentTblEntity assignmentTbl;

	/** テスト番号. */
	private Integer no;

	/** テストの入力値. */
	private String inputText;

	/** 出力値. */
	private String outputTxt;

	/**
	 * コンストラクタ.
	 */
	public TestCaseTblEntity() {
	}

	/**
	 * testcase_id を設定します.
	 * 
	 * @param testcaseId
	 *            testcase_id
	 */
	public void setTestcaseId(Integer testcaseId) {
		this.testcaseId = testcaseId;
	}

	/**
	 * testcase_id を取得します.
	 * 
	 * @return testcase_id
	 */
	public Integer getTestcaseId() {
		return this.testcaseId;
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
	 * テスト番号 を設定します.
	 * 
	 * @param no
	 *            テスト番号
	 */
	public void setNo(Integer no) {
		this.no = no;
	}

	/**
	 * テスト番号 を取得します.
	 * 
	 * @return テスト番号
	 */
	public Integer getNo() {
		return this.no;
	}

	/**
	 * テストの入力値 を設定します.
	 * 
	 * @param inputText
	 *            テストの入力値
	 */
	public void setInputText(String inputText) {
		this.inputText = inputText;
	}

	/**
	 * テストの入力値 を取得します.
	 * 
	 * @return テストの入力値
	 */
	public String getInputText() {
		return this.inputText;
	}

	/**
	 * 出力値 を設定します.
	 * 
	 * @param outputTxt
	 *            出力値
	 */
	public void setOutputTxt(String outputTxt) {
		this.outputTxt = outputTxt;
	}

	/**
	 * 出力値 を取得します.
	 * 
	 * @return 出力値
	 */
	public String getOutputTxt() {
		return this.outputTxt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((testcaseId == null) ? 0 : testcaseId.hashCode());
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
		TestCaseTblEntity other = (TestCaseTblEntity) obj;
		if (testcaseId == null) {
			if (other.testcaseId != null) {
				return false;
			}
		} else if (!testcaseId.equals(other.testcaseId)) {
			return false;
		}
		return true;
	}

}
