package jp.ac.asojuku.azcafe.dto.subclass;

import lombok.Data;

@Data
public class FollowElementDto {
	private Integer userId;
	private String nickName;
	private String hoomroomName;
	private String avater;
	private Boolean isEach;	//相互フォローフラグ
}
