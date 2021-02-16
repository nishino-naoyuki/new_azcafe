package jp.ac.asojuku.azcafe.dto;

import java.util.Date;

import jp.ac.asojuku.azcafe.param.InfoKind;
import lombok.Data;

/**
 * お知らせ情報
 * @author nishino
 *
 */
@Data
public class InfomationDto {
	private InfoKind infoKind;
	private String message;
	private boolean isNew;
	private Date date;
	
	public String getInfoKind() {
		return infoKind.getTitle();
	}
	
	public String getInfoPanel() {
		return infoKind.getPanel();
	}
}
