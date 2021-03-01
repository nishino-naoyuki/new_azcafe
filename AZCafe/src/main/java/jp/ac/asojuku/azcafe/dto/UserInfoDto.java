package jp.ac.asojuku.azcafe.dto;

import java.util.ArrayList;
import java.util.List;

import jp.ac.asojuku.azcafe.dto.subclass.AssignmentResultDto;
import jp.ac.asojuku.azcafe.dto.subclass.FollowElementDto;
import jp.ac.asojuku.azcafe.param.RoleId;
import lombok.Data;

@Data
public class UserInfoDto {
	private Integer userId;
	private String OrgNo;
	private String homeroomeName;
	private String nickName;
	private String name;
	private String level;
	private List<LevelDto> levelList;
	private Integer followNum;
	private Integer followerNum;
	private String avater;
	private RoleId role;
	private Boolean isFollowUser;	//このユーザーをフォローしているかどうか（本人の場合はtrue）
	private List<FollowElementDto> followList = new ArrayList<>();
	private List<FollowElementDto> followerList = new ArrayList<>();
	private List<AssignmentResultDto> assignmentRetList = new ArrayList<>();
	private List<SkillMapDto> skillMap = new ArrayList<>();
	
	public void addFollowElementDto(FollowElementDto dto) {
		followList.add(dto);
	}
	public void addFollowerElementDto(FollowElementDto dto) {
		followerList.add(dto);
	}
	public void addAssignmentResultDto(AssignmentResultDto dto) {
		assignmentRetList.add(dto);
	}
	public void addSkillMapDto(SkillMapDto dto) {
		skillMap.add(dto);
	}
}
