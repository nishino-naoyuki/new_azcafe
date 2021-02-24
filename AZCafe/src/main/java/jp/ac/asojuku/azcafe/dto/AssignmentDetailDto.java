package jp.ac.asojuku.azcafe.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AssignmentDetailDto extends AssignmentElementDto {

	private String content;
	private String answer;
	private List<SkillDto> skillList;
	private GradingResultDto gradingResultDto;
	
	public String getSkillSetting() {
		StringBuffer sb = new StringBuffer();
		for( SkillDto dto : skillList ) {
			sb.append("・").append(dto.getName()).append(" ");
		}
		return sb.toString();
	}
}
