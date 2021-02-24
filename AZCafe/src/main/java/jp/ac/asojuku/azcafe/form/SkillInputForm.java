package jp.ac.asojuku.azcafe.form;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class SkillInputForm {

	private Integer id;
	
	@NotEmpty(message = "{errmsg0105}")
	private String name;
}
