package jp.ac.asojuku.azcafe.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.azcafe.config.ValidationConfig;
import jp.ac.asojuku.azcafe.dto.ResultDto;
import jp.ac.asojuku.azcafe.dto.SkillDto;
import jp.ac.asojuku.azcafe.dto.SkillUpdateDto;
import jp.ac.asojuku.azcafe.err.ErrorCode;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.form.SkillInputForm;
import jp.ac.asojuku.azcafe.service.SkillService;

@RestController
@RequestMapping(value= {"/skill"})
public class SkillMainte {

	@Autowired
	SkillService skillService;
	
	/**
	 *スキル名の更新
	 * @param homeroomInputForm
	 * @param err
	 * @return
	 * @throws JsonProcessingException 
	 * @throws AZCafeException 
	 */
	@RequestMapping(value= {"/update"}, method=RequestMethod.POST)
	public Object update(
			@Valid SkillInputForm skillInputForm,BindingResult err
			) throws JsonProcessingException, AZCafeException {
		ResultDto<SkillDto> result = null;

		if( skillService.isDuplicateName(skillInputForm.getName())) {
			//重複
			result = new ResultDto<>(false,ValidationConfig.getInstance().getMsg(ErrorCode.ERR_SKILL_NAME_DUPLICATE),null);
			return getJson(result);
		}
		SkillUpdateDto dto = new SkillUpdateDto();
		
		dto.setDel(false);
		dto.setSkillId(skillInputForm.getId());
		dto.setName(skillInputForm.getName());
		//スキル情報を更新
		SkillDto resultDto = skillService.insertOrUpdate(dto) ;

		return getJson(
				new ResultDto<>(true,"",resultDto)
				);
	}

	/**
	 * スキルの更新
	 * 
	 * @param name
	 * @return
	 * @throws JsonProcessingException 
	 * @throws AZCafeException 
	 */
	@RequestMapping(value= {"/insert"}, method=RequestMethod.POST)
	public Object insert(String name) throws JsonProcessingException, AZCafeException {
		ResultDto<SkillDto> result = null;

		if( skillService.isDuplicateName(name)) {
			//重複
			result = new ResultDto<>(false,ValidationConfig.getInstance().getMsg(ErrorCode.ERR_SKILL_NAME_DUPLICATE),null);
			return getJson(result);
		}
		
		SkillUpdateDto dto = new SkillUpdateDto();
		
		dto.setDel(false);
		dto.setSkillId(null);
		dto.setName(name);
		//スキル情報を追加
		SkillDto resultDto = skillService.insertOrUpdate(dto);
				
		return getJson(
				new ResultDto<>(true,"",resultDto)
				);
	}
	
	private String getJson(ResultDto<SkillDto> result ) throws JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(result);
        
        return jsonString;
	}
}
