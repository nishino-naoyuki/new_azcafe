package jp.ac.asojuku.azcafe.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GradingResultDetailDto {
	private String title;
	private Integer answerId;
	private Integer assignmentId;
	private GradingResultDto gradingResultDto;
	private List<SourceCodeDto> sourceCodeList;
	private List<CommentDto> commentList;
	private Integer goodCount;
	
	public void addComment(CommentDto comment) {
		if( commentList == null ) {
			commentList = new ArrayList<>();
		}
		commentList.add(comment);
	}
	
	public void addSourceCode(SourceCodeDto sourceCode) {
		if( sourceCodeList == null ) {
			sourceCodeList = new ArrayList<>();
		}
		sourceCodeList.add(sourceCode);
	}
}
