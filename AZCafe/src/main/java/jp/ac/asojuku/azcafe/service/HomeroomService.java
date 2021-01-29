package jp.ac.asojuku.azcafe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.azcafe.dto.HomeroomDto;
import jp.ac.asojuku.azcafe.entity.HomeroomTblEntity;
import jp.ac.asojuku.azcafe.repository.HomeroomRepository;

@Service
public class HomeroomService {

	@Autowired
	HomeroomRepository homeroomRepository;
	
	/**
	 * クラスリストを取得する
	 * 
	 * @return
	 */
	public List<HomeroomDto> getAll(){
		List<HomeroomDto> dtoList = new ArrayList<>();
		
		//Entity取得
		List<HomeroomTblEntity> entityList = homeroomRepository.findAll();
		
		//entity -> dto
		for( HomeroomTblEntity entity : entityList) {
			HomeroomDto dto = new HomeroomDto();
			
			dto.setHomeroomId(entity.getHomeroomId());
			dto.setHoroomeName(entity.getName());
			dto.setCourseName(entity.getCourseTbl().getName());
			
			dtoList.add(dto);
		}
		
		return dtoList;
	}
	
	/**
	 * クラスの更新
	 * @param id
	 * @param name
	 */
	public void update(Integer id,String name) {
		HomeroomTblEntity homeroomTblEntity = homeroomRepository.getOne(id);
		
		homeroomTblEntity.setName(name);
		
		homeroomRepository.saveAndFlush(homeroomTblEntity);
	}
}
