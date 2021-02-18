package jp.ac.asojuku.azcafe.csv;


import lombok.Data;

@Data
public class UserAnswerCSV {
	StringBuilder sb;
	
	public void addData(String info) {
		if( sb == null ) {
			sb = new StringBuilder("\""+info+"\"");
		}else {
			sb.append(",").append("\""+info+"\"");
		}
	}
	
	public String toString() {
		if( sb == null ) {
			return "";
		}
		return sb.toString();
	}
}
