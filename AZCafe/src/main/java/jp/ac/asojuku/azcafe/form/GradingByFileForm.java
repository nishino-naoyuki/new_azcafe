package jp.ac.asojuku.azcafe.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class GradingByFileForm {
	private Integer assignmentId;
	private MultipartFile answerFile1;
	private MultipartFile answerFile2;
	private MultipartFile answerFile3;
	private MultipartFile answerFile4;
	private MultipartFile answerFile5;
}
