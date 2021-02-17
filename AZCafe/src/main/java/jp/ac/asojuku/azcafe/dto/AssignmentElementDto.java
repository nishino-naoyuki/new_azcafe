package jp.ac.asojuku.azcafe.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.ac.asojuku.azcafe.param.Difficulty;
import lombok.Data;

/**
 * 課題一覧表示用の要素保持クラス
 * @author nishino
 *
 */
@Data
public class AssignmentElementDto {

	private Integer assignmentId;
	private String groupName;
	private String title;
	private Integer score;
	private Difficulty difficulty;
	private boolean correct;
	private Date updateDate;
	private Integer point;
	private Integer handNum;
	
	public void setDifficulty(Integer difficulty) {
		this.difficulty = Difficulty.getBy(difficulty);
	}
	public String getDifficulty() {
		return difficulty.getMsg();
	}

	public String getState() {
		String msg = (correct ? "提出済":"未提出");
		msg += "("+handNum+")";
		return msg;
	}

	public String getScore() {
		return (score == null ? "0":score.toString());
	}

	public String getUpdateDate() {
		if( updateDate != null ) {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	        return sdf.format(updateDate);
		}else {
			return "----";
		}
	}
}
