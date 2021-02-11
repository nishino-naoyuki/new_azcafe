package jp.ac.asojuku.azcafe.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

/**
 * @author nishino
 *
 */
@Data
public class CommentDto {
	private Integer userId;
	private String userName;
	private String avater;
	private String message;
	private Date commentDate;
	
	public String getCommentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return sdf.format(commentDate);
	}
}
