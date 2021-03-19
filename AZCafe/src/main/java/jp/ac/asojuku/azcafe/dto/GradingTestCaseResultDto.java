package jp.ac.asojuku.azcafe.dto;

import org.apache.commons.lang3.StringUtils;

import jp.ac.asojuku.azcafe.util.HtmlUtil;
import lombok.Data;

/**
 * テストケースごとの結果
 * @author nishino
 *
 */
@Data
public class GradingTestCaseResultDto {
	boolean correct;	//正解フラグ
	Integer testcaseId;
	String input;	//入力値
	String userOutput;	//ユーザーの出力
	String correctOutput;	//正解出力
	
	public String getInput() {
		if( StringUtils.isEmpty(input) ) {
			return "<入力無し>";
		}
		return HtmlUtil.nl2be(input);
	}
	
	public String getCorrectOutputHtml() {
		return  HtmlUtil.nl2be(correctOutput);
	}
	
	public String getUserOutputHtml() {
		return  HtmlUtil.nl2be(userOutput);
	}
}
