package jp.ac.asojuku.azcafe.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserInputCSVForm {
	private MultipartFile uploadFile;
}
