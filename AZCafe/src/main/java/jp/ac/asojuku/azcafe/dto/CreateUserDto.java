package jp.ac.asojuku.azcafe.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * ユーザー登録のDTO
 * @author nishino
 *
 */
@Data
public class CreateUserDto {
	private String iconFileName;	
	private Integer roleId;	
	private String studentNo;	
	private String mail;
	private String nickname;
	private Integer admissionYear;
	private String pass;
	private Integer homeroomId;
}
