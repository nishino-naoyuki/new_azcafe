package jp.ac.asojuku.azcafe.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 判定結果
 * @author nishino
 *
 */
@Data
public class GradingResult {
	boolean correct;	//正解フラグ
	String compleErrMsg;	//コンパイルエラー
	int scoreForOutput;	//出力結果の点数
	int scoreForSource;	//ソースコードの点数
	List<GradingTestCaseResult> testCaseResultList = null;
	List<String> checkStyleMsgList = null;
	
	public void addGradingTestCaseResult(GradingTestCaseResult testCaseRet) {
		if( testCaseResultList == null ) {
			testCaseResultList = new ArrayList<>();
		}
		testCaseResultList.add(testCaseRet);
	}
	
	public void addCheckStyleMsgList(String checkStyleMsg) {
		if( checkStyleMsgList == null ) {
			checkStyleMsgList = new ArrayList<>();
		}
		checkStyleMsgList.add(checkStyleMsg);
	}
}
