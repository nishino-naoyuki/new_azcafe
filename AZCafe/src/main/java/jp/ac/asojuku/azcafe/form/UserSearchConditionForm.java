package jp.ac.asojuku.azcafe.form;


import lombok.Data;

@Data
public class UserSearchConditionForm {
	public final static Integer NO_SELECT = -1;
	private String nickname;
	private String mail;
	private Integer homeroomId;
	private Integer roleId;
	private Integer level;
	private String name;
}
