package jp.ac.asojuku.azcafe.dto;

import lombok.Data;

@Data
public class UserSearchElementDto {
	private Integer userId;
	private String OrgNo;
	private String homeroomeName;
	private String nickName;
	private String level;
	private Integer followNum;
	private Integer followerNum;
	private String avater;
}
