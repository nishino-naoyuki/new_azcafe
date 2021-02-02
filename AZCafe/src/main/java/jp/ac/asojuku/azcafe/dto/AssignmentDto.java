package jp.ac.asojuku.azcafe.dto;

import java.util.ArrayList;
import java.util.List;

import jp.ac.asojuku.azcafe.form.AssignmentTestCaseForm;
import jp.ac.asojuku.azcafe.form.AssignmentPublicForm;
import jp.ac.asojuku.azcafe.param.Difficulty;
import lombok.Data;

@Data
public class AssignmentDto {

	private String group;
	
	private String title;
	
	private Difficulty difficulty;
	
	private String content;

	private List<AssignmentPublicDto>  publicStateList;
	
	private List<AssignmentTestCaseDto> answerList;
	
	public void setDifficulty(Integer difficulty) {
		this.difficulty = Difficulty.getBy(difficulty);
	}
	public String getDifficulty() {
		return difficulty.getMsg();
	}
	public Integer getDifficultyAsInt() {
		return difficulty.getId();
	}
	
	//form->Dto for List
	public void setPublicStateList(List<AssignmentPublicForm> formList) {
		List<AssignmentPublicDto>  publicStateList = new ArrayList<>();
		
		for(AssignmentPublicForm form : formList) {
			AssignmentPublicDto dto = new AssignmentPublicDto();
			
			dto.setHomeroomId(form.getHomeroomId());
			dto.setPublicState(form.getPublicState());
			dto.setHomeroomName(form.getHomeroomName());
			
			publicStateList.add(dto);
		}
		
		this.publicStateList = publicStateList;
	}
	
	//form->Dto for List
	public void setAnswerList(List<AssignmentTestCaseForm> formList) {
		List<AssignmentTestCaseDto> answerList = new ArrayList<>();
		
		for( AssignmentTestCaseForm form : formList) {
			AssignmentTestCaseDto dto = new AssignmentTestCaseDto();
			
			dto.setInput(form.getInput());
			dto.setOutput(form.getOutput());
			
			answerList.add(dto);
		}
		
		this.answerList = answerList;
	}
}
