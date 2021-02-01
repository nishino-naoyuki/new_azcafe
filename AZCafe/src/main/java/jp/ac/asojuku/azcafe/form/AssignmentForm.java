package jp.ac.asojuku.azcafe.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 課題フォーム
 * @author nishino
 *
 */
@Data
public class AssignmentForm {

	@NotEmpty(message = "{errmsg0301}")
	@Size(max = 30, message="{errmsg0304}")
	private String group;
	
	@NotEmpty(message = "{errmsg0302}")
	@Size(max = 100, message="{errmsg0305}")
	private String title;
	
	private Integer dificalty;
	
	@NotEmpty(message = "{errmsg0303}")
	private String content;
	
	private List<AssignmentPublicForm>  publicStateList;
	
	private List<AssignmentAnswerForm> answerList;
	
}
