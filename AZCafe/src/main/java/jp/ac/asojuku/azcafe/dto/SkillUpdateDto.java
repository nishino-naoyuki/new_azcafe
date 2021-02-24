package jp.ac.asojuku.azcafe.dto;

import lombok.Data;

/**
 * スキルの追加・更新用DTO
 * @author nishino
 *
 */
@Data
public class SkillUpdateDto {
	private boolean isDel;	//削除フラグこれがtrueのときは削除をしようとする、IDが見つからなければ何もしない
	private Integer skillId;	//新規追加の時はnull
	private String name;
}
