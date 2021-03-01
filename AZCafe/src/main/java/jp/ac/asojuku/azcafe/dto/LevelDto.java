package jp.ac.asojuku.azcafe.dto;

import jp.ac.asojuku.azcafe.param.LevelRank;
import lombok.Data;

@Data
public class LevelDto {
	private Integer levelId;
	private String name;
	private String description;
	private LevelRank rank; 
	
	public String getRankClass() {
		if( rank != null ) {
			return rank.getMsg();
		}
		return "";
	}
}
