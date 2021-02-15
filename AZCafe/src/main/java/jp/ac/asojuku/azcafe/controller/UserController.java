package jp.ac.asojuku.azcafe.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.azcafe.dto.HomeroomDto;
import jp.ac.asojuku.azcafe.dto.LevelDto;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.dto.UserInfoDto;
import jp.ac.asojuku.azcafe.dto.UserSearchElementDto;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.form.UserSearchConditionForm;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.HomeroomService;
import jp.ac.asojuku.azcafe.service.LevelService;
import jp.ac.asojuku.azcafe.service.UserService;

/**
 * ユーザー情報コントローラ
 * 
 * @author nishino
 *
 */
@Controller
 @RequestMapping(value= {"/user"})
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
			
	@Autowired
	HomeroomService homeroomService;
	@Autowired
	LevelService levelService;
	@Autowired
	UserService userService;
	@Autowired
	HttpSession session;
	
	@RequestMapping(value= {"/search"}, method=RequestMethod.GET)
	public ModelAndView search(
			ModelAndView mv) {
		//クラス一覧取得
		List<HomeroomDto> list = homeroomService.getAll();
		//称号一覧の取得
		List<LevelDto> levelList = levelService.getList();
		
		mv.addObject("levelList", levelList);
		mv.addObject("homeroomList", list);
		mv.addObject("userSearchConditionForm", new UserSearchConditionForm());
		mv.setViewName("user_search");
		return mv;
	}
	
	@RequestMapping(value= {"/search"}, method=RequestMethod.POST)
	@ResponseBody
	public Object execSearch(
			@Valid UserSearchConditionForm cond
			) throws JsonProcessingException {
		
		List<UserSearchElementDto> list = userService.getList(cond);

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(list);
		
        logger.trace("jsonString:{}",jsonString);
        
		return jsonString;
	}

	@RequestMapping(value= {"/follow"}, method=RequestMethod.POST)
	@ResponseBody
	public Object follow(@RequestParam(required = false) Integer userId) throws AZCafeException {

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		
		userService.insertFollow(loginInfo.getUserId(), userId);
		
		return "ok";
	}

	@RequestMapping(value= {"/info"}, method=RequestMethod.GET)
	public ModelAndView info(
			ModelAndView mv,
			@RequestParam(required = false) Integer userId
			) throws AZCafeException {

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		
		//ユーザー情報を取得する
		UserInfoDto userInfoDto = userService.getInfo(
				loginInfo.getUserId(),
				userId);
		
		mv.addObject("userInfoDto",userInfoDto);
		mv.setViewName("user_info");
		return mv;
	}
}
