package jp.ac.asojuku.azcafe.dto;

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
	
	public void setDifficulty(Integer difficulty) {
		this.difficulty = Difficulty.getBy(difficulty);
	}
	public String getDifficulty() {
		return difficulty.getMsg();
	}
	
	public String getScore() {
		return (score == null ? "未提出":score.toString());
	}
}
