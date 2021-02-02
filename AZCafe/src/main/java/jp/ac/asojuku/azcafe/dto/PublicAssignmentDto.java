package jp.ac.asojuku.azcafe.dto;

import java.util.Date;

import lombok.Data;

/**
 * @author nishino
 *
 */
@Data
public class PublicAssignmentDto {
	private Integer homeroomId;
	private String homeroomName;
	/** 公開設定. */
	private Integer publicState;

	/** 公開開始日時. */
	private Date publicStartDate;

	/** 公開終了日時. */
	private Date publicEndDate;
}
