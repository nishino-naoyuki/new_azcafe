package jp.ac.asojuku.azcafe.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserInputForm {

	private MultipartFile icon;
	
	private Integer roleId;
	
	private String studentNo;
	
	@NotEmpty(message = "{errmsg0105}")
	@Size(max = 60, message="{errmsg0305}")
	private String mail;

	@NotEmpty(message = "{errmsg0112}")
	@Size(max = 100, message="{errmsg0113}")
	private String nickname;

	@NotEmpty(message = "{errmsg0125}")
	private String admissionYear;
	
	private String pass1;
	private String pass2;
	
	private Integer homeroomId;
}
