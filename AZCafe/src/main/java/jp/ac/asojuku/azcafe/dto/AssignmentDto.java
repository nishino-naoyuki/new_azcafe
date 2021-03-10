package jp.ac.asojuku.azcafe.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.ac.asojuku.azcafe.form.AssignmentTestCaseForm;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.form.AssignmentPublicForm;
import jp.ac.asojuku.azcafe.param.Difficulty;
import lombok.Data;

@Data
public class AssignmentDto {
	private Integer assignmentId;//更新用。新規の時はnull

	private String group;
	
	private String title;
	
	private Difficulty difficulty;
	
	private String content;

	private Date updateDate;
	
	private List<AssignmentPublicDto>  publicStateList;
	
	private List<AssignmentTestCaseDto> answerList;
	
	private List<Integer> skillIdList;
	private String skillIdString;	//表示用
	
	public void setDifficulty(Integer difficulty) {
		this.difficulty = Difficulty.getBy(difficulty);
	}
	public String getDifficulty() {
		return difficulty.getMsg();
	}
	public Integer getDifficultyAsInt() {
		return difficulty.getId();
	}
	
	public void addAssignmentPublicDto(AssignmentPublicDto dto) {
		if( publicStateList == null ) {
			publicStateList = new ArrayList<>();
		}
		publicStateList.add(dto);
	}
	public void addAssignmentTestCaseDto(AssignmentTestCaseDto dto) {
		if( answerList == null ) {
			answerList = new ArrayList<>();
		}
		answerList.add(dto);
	}
	public void addSkillId(Integer skillId ) {
		if( skillIdList == null ) {
			skillIdList = new ArrayList<>();
		}
		skillIdList.add(skillId);
	}
	public void addSkillId(List<String> strIdList) throws AZCafeException {
		if( strIdList == null ) {
			skillIdList = new ArrayList<>();
			return;
		}
		try {
			for(String id : strIdList) {
				int intSkillId = Integer.parseInt(id);
				if( skillIdList == null ) {
					skillIdList = new ArrayList<>();
				}
				skillIdList.add(intSkillId);
			}
		}catch(Exception e) {
			//数値しか入らないはずなのに変換できない？！
			throw new AZCafeException(e);
		}
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
