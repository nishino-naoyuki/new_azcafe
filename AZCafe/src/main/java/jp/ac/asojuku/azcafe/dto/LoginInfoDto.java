package jp.ac.asojuku.azcafe.dto;

import java.text.NumberFormat;

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
	private int goodNum;
	private int followNum;
	private int followerNum;
	private int submittedQNum;	//提出済みの問題数
	
	public String getGoodNum() {
		return NumberFormat.getNumberInstance().format(goodNum);
	}

	public String getGollowNum() {
		return NumberFormat.getNumberInstance().format(followNum);
	}

	public String getFollowerNum() {
		return NumberFormat.getNumberInstance().format(followerNum);
	}
}
