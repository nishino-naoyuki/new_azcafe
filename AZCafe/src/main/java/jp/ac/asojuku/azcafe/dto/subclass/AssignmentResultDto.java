package jp.ac.asojuku.azcafe.dto.subclass;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class AssignmentResultDto {
	private Integer assigmentId;
	private String title;
	private String group;
	private Date answerDate;
	private Integer goodNum;
	private Integer commentNum;
	private Integer score;
	private Boolean isCorrect;	//この答えが他人の答えだった場合に自分が提出済みかどうかのフラグ
	
	public String getAnswerDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return sdf.format(answerDate);
	}
}
