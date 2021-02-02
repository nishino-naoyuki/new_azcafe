package jp.ac.asojuku.azcafe.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class AssignmentTestCaseForm {
	private String input;
	@NotEmpty(message = "{errmsg0308}")
	private String output;
}
