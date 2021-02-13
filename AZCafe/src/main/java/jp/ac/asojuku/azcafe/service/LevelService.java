package jp.ac.asojuku.azcafe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.azcafe.dto.LevelDto;
import jp.ac.asojuku.azcafe.entity.LevelTblEntity;
import jp.ac.asojuku.azcafe.repository.LevelRepository;

@Service
public class LevelService {
	@Autowired
	LevelRepository levelRepository;
	
	/**
	 * 称号の一覧を取得する
	 * @return
	 */
	public List<LevelDto> getList(){
		List<LevelDto> list = new ArrayList<>();
		List<LevelTblEntity> entityList =  levelRepository.findAll(Sort.by(Sort.Direction.ASC, "levelId"));
		
		for(LevelTblEntity entity : entityList) {
			LevelDto dto = new LevelDto();
			
			dto.setLevelId(entity.getLevelId());
			dto.setName(entity.getName());
			dto.setDescription(entity.getDescription());
			
			list.add(dto);
		}
		
		return list;
	}
}
