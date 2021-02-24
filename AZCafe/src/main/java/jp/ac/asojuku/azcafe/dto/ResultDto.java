package jp.ac.asojuku.azcafe.dto;

import lombok.Data;

@Data
public class ResultDto<T> {
		private boolean success;
		private String message;
		private T resultInfo;
		
		public ResultDto(boolean success,String message,T resultInfo) {
			this.success = success;
			this.message = message;
			this.resultInfo = resultInfo;
		}
}
