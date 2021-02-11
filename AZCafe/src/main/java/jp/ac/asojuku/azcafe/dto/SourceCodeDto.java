package jp.ac.asojuku.azcafe.dto;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class SourceCodeDto {
	private String fileName;
	private String code;
	
	public String getFileName() {
		if( StringUtils.isEmpty(fileName)) {
			return "直接入力";
		}
		return fileName;
	}
	
	public String getCodeForHtml() {
		return code.replace("\n", "<br>").replace(" ", "&nbsp;").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
	}
}
