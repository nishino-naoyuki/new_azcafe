package jp.ac.asojuku.azcafe.dto;

import lombok.Data;

/**
 * 判定結果
 * @author nishino
 *
 */
@Data
public class GradingResult {
	boolean bCorrect;	//正解フラグ
	int scoreForOutput;	//出力結果の点数
	int scoreForSource;	//ソースコードの点数
	
	
}
