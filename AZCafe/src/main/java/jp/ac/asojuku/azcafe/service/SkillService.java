package jp.ac.asojuku.azcafe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.azcafe.dto.SkillDto;
import jp.ac.asojuku.azcafe.dto.SkillUpdateDto;
import jp.ac.asojuku.azcafe.entity.SkillTblEntity;
import jp.ac.asojuku.azcafe.repository.SkillRepository;

@Service
public class SkillService {
	
	@Autowired
	SkillRepository skillRepository;

	/**
	 * スキル情報の取得
	 * @return
	 */
	public List<SkillDto> getAll(){
		List<SkillDto> list = new ArrayList<>();
		
		List<SkillTblEntity> entityList = skillRepository.findAll(Sort.by("skillId"));
		
		//entity -> dto
		for(SkillTblEntity entity : entityList ) {
			SkillDto dto = new SkillDto();
			
			dto.setSkillId(entity.getSkillId());
			dto.setName(entity.getName());
			dto.setUpdateDate(entity.getUpdateDate());
			
			list.add(dto);
		}
		
		return list;
	}
	
	/**
	 * スキル情報の追加・更新を行う
	 * 
	 * @param dto
	 * @return
	 */
	public SkillDto insertOrUpdate(SkillUpdateDto dto) {
		SkillTblEntity entity = null;
		
		if( dto.getSkillId() != null ) {
			entity = skillRepository.getOne(dto.getSkillId());
		}
		
		if( entity == null ) {
			entity = new SkillTblEntity();
		}
		
		entity.setName(dto.getName());
		
		entity = skillRepository.save(entity);

		SkillDto resultDto = new SkillDto();
		
		resultDto.setSkillId(entity.getSkillId());
		resultDto.setName(entity.getName());
		resultDto.setUpdateDate(entity.getUpdateDate());
		
		return resultDto;
	}
	
	/**
	 * スキル名の重複をチェックする
	 * 
	 * @param name
	 * @return
	 */
	public boolean isDuplicateName(String name) {
		boolean result = false;
		List<SkillTblEntity> entityList = skillRepository.findAll(Sort.by("skillId"));
		
		for(SkillTblEntity entity : entityList ) {
			String chkName = entity.getName();
			//空白を削除
			name = name.replaceAll(" ","").replaceAll("　", "");
			chkName = chkName.replaceAll(" ","").replaceAll("　", "");
			//大文字小文字を区別せずに比較する
			if( name.equalsIgnoreCase(chkName)) {
				result = true;
				break;
			}
		}
		
		return result;
	}
}
