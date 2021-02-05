package jp.ac.asojuku.azcafe.form;

import lombok.Data;

@Data
public class GradingByTextForm {
	private Integer language;
	private String answerText;
	private Integer assignmentId;
}
