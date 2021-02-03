package jp.ac.asojuku.azcafe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.azcafe.dto.GroupDto;
import jp.ac.asojuku.azcafe.entity.GroupTblEntity;
import jp.ac.asojuku.azcafe.repository.GroupRepository;

/**
 * グループサービス
 * @author nishino
 *
 */
@Service
public class GroupService {
	@Autowired
	GroupRepository groupRepository;

	/**
	 * 課題グループの一覧を取得する
	 * 
	 * @return
	 */
	public List<GroupDto> getAll(){
		List<GroupDto> dtoList = new ArrayList<>();
		
		List<GroupTblEntity> entityList = groupRepository.findAll();
		
		//entity -> dto
		for( GroupTblEntity entity : entityList ) {
			GroupDto dto = new GroupDto();
			
			dto.setGroupId(entity.getGroupId());
			dto.setName(entity.getName());
			
			dtoList.add(dto);
		}
		
		return dtoList;
	}
	
}
