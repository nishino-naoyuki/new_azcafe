package jp.ac.asojuku.azcafe.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserSearchElementDto {
	private Integer userId;
	private String OrgNo;
	private String homeroomeName;
	private String nickName;
	private List<LevelDto> levelList;
	private Integer followNum;
	private Integer followerNum;
	private String avater;
	private String name;
}
