package jp.ac.asojuku.azcafe.dto;

import lombok.Data;

@Data
public class CommentInsertDto {
	private Integer answerId;
	private Integer assignmentId;
	private Integer userId;
	private String message;

}
