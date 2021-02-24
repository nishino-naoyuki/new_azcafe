package jp.ac.asojuku.azcafe.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SkillDto {

	private Integer skillId;
	private String name;
	private Date updateDate;
}
