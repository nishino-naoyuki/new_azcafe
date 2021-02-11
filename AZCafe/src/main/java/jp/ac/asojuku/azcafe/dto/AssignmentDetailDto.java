package jp.ac.asojuku.azcafe.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AssignmentDetailDto extends AssignmentElementDto {

	private String content;
	private String answer;
	private GradingResultDto gradingResultDto;
}
