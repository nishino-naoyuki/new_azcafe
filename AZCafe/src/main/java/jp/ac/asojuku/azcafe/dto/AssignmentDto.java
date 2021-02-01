package jp.ac.asojuku.azcafe.dto;

import java.util.List;

import lombok.Data;

@Data
public class AssignmentDto {

	private String group;
	
	private String title;
	
	private Integer dificalty;
	
	private String content;

	private List<AssignmentPublicDto>  publicStateList;
	
	private List<AssignmentAnswerDto> answerList;
}
