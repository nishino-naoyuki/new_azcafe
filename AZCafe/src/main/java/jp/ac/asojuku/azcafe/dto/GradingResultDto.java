package jp.ac.asojuku.azcafe.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * 判定結果
 * @author nishino
 *
 */
@Data
public class GradingResultDto {
	boolean correct;	//正解フラグ
	String compleErrMsg;	//コンパイルエラー
	int scoreForOutput;	//出力結果の点数
	int scoreForSource;	//ソースコードの点数
	List<GradingTestCaseResultDto> testCaseResultList = new ArrayList<>();
	String checkStyleMsg;
	int point;
	
	public void addGradingTestCaseResult(GradingTestCaseResultDto testCaseRet) {
		testCaseResultList.add(testCaseRet);
	}
		
	public String getCheckStyleMsg() {
		if( StringUtils.isEmpty(checkStyleMsg) ) {
			return "指摘事項無し";
		}
		return checkStyleMsg;
	}
}
