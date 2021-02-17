package jp.ac.asojuku.azcafe.dto;

import lombok.Data;

@Data
public class RankingDto {
	private Integer rank;
	private Integer userId;
	private String avater;
	private String nickName;
	private Integer point;
	private String homeroomeName;
}
