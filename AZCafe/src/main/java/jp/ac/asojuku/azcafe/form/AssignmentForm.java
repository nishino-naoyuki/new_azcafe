package jp.ac.asojuku.azcafe.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jp.ac.asojuku.azcafe.dto.AssignmentTestCaseDto;
import jp.ac.asojuku.azcafe.dto.AssignmentPublicDto;
import jp.ac.asojuku.azcafe.dto.HomeroomDto;
import lombok.Data;

/**
 * 課題フォーム
 * @author nishino
 *
 */
@Data
public class AssignmentForm {
	
	private Integer assignmentId;
	private Integer ansCount;

	@NotEmpty(message = "{errmsg0301}")
	@Size(max = 30, message="{errmsg0304}")
	private String group;
	
	@NotEmpty(message = "{errmsg0302}")
	@Size(max = 100, message="{errmsg0305}")
	private String title;
	
	private Integer difficulty;
	
	@NotEmpty(message = "{errmsg0303}")
	@Size(max = 30000, message="{errmsg0306}")
	private String content;
	
	private List<AssignmentPublicForm>  publicStateList;
	
	@NotNull(message = "{errmsg0307}")
	@Valid
	private List<AssignmentTestCaseForm> answerList;
	
	private List<String> skillIdList;
	
	//公開設定初期化用メソッド
	public void initPublicStateList(List<HomeroomDto> homeroomList) {
		if( publicStateList == null ) {
			publicStateList = new ArrayList<>();
		}
		
		for( HomeroomDto dto : homeroomList ) {
			AssignmentPublicForm form = new AssignmentPublicForm();
			
			form.setHomeroomId(dto.getHomeroomId());
			form.setHomeroomName(dto.getHomeroomName());
			//stateは未設定にすることよにり0(＝未公開）が入る
			
			publicStateList.add(form);
		}
	}

	/**
	 * 答えリストをDto→Form変換する
	 * @param answerList
	 */
	public void setAnswerList(List<AssignmentTestCaseDto> answerList) {
		this.answerList = new ArrayList<>();
		
		for(AssignmentTestCaseDto dto : answerList) {
			AssignmentTestCaseForm form = new AssignmentTestCaseForm();
			form.setInput(dto.getInput());
			form.setOutput(dto.getOutput());
			
			this.answerList.add(form);
		}
		
	}

	/**
	 * 工科リストをDto→Form変換
	 * @param publicStateList
	 */
	public void setPublicStateList(List<AssignmentPublicDto> publicStateList) {
		this.publicStateList = new ArrayList<>();
		
		for( AssignmentPublicDto dto : publicStateList) {
			AssignmentPublicForm form = new AssignmentPublicForm();
			
			form.setHomeroomId(dto.getHomeroomId());
			form.setHomeroomName(dto.getHomeroomName());
			form.setPublicState(dto.getPublicState());
			
			this.publicStateList.add(form);
		}
		
	}
}
