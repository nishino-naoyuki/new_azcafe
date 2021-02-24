package jp.ac.asojuku.azcafe.form;

import java.util.List;

import lombok.Data;

@Data
public class AssignmentUpdatePublicForm {

	private String idlist;	//idのList（CSV形式）
	private List<AssignmentPublicForm>  publicStateList;
}
