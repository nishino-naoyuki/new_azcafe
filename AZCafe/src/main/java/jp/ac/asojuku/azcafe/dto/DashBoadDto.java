package jp.ac.asojuku.azcafe.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.Data;

/**
 * ダッシュボード情報
 * @author nishino
 *
 */
@Data
public class DashBoadDto {
	
	//ソート用クラス
	private class InfoComparator implements Comparator<InfomationDto>{

		@Override
		public int compare(InfomationDto o1, InfomationDto o2) {
			if( o1.getDate() == null || o2.getDate() == null ) {
				return 0;
			}
			return o2.getDate().compareTo(o1.getDate());
		}
		
	}
	
	private String level;
	private List<LevelDto> levelList =  new ArrayList<>();;
	private Integer answerNum;
	private Integer assignmentNum;
	private Integer goodNum;
	private Integer followNum;
	private Integer followerNum;
	private Integer point;
	private List<InfomationDto> newInfoList =  new ArrayList<>();;
	
	public void addInfomationDto(InfomationDto info) {
		newInfoList.add(info);
	}
	
	public void sortInfomation() {
		if( newInfoList != null ) {
			Collections.sort(newInfoList,new InfoComparator());
		}
	}
	
	public void addLevelList(LevelDto dto) {
		levelList.add(dto);
	}
	
}
