package jp.ac.asojuku.azcafe.dto;

import lombok.Data;

/**
 * テストケースごとの結果
 * @author nishino
 *
 */
@Data
public class GradingTestCaseResult {
	boolean correct;	//正解フラグ
	Integer testcaseId;
	String userOutput;	//ユーザーの出力
	String correctOutput;	//正解出力
}
