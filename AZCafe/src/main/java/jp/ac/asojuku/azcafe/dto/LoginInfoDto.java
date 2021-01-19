package jp.ac.asojuku.azcafe.dto;

import jp.ac.asojuku.azcafe.param.RoleId;
import lombok.Data;

@Data
public class LoginInfoDto {

	private int userId;
	private String mail;
	private String nickName;
	private RoleId role;
	private String roleName;
	private String courseName;
	private int grade;
	private String levelName;
	private String avater;
}
