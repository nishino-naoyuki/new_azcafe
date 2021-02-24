package jp.ac.asojuku.azcafe.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.dto.UserInfoDto;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.UserService;


@RestController
public class SkillMapController {
	
	@Autowired
	HttpSession session;
	@Autowired
	UserService userService;

	/**
	 * スキルマップを表示する
	 * @param mv
	 * @param userId
	 * @return
	 * @throws AZCafeException
	 */
	@RequestMapping(value= {"/skillmap"}, method=RequestMethod.GET)
	public ModelAndView skillmap(ModelAndView mv,@RequestParam(required=false) Integer userId) throws AZCafeException {

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null  ) {
			throw new AZCafeException("不正なアクセスです");
		}
		
		if( userId == null ) {
			userId = loginInfo.getUserId();
		}
		
		//ユーザー情報（の中にスキルマップがある）を取得する
		UserInfoDto userInfo = 
				userService.getInfo(loginInfo.getUserId(), userId);
		
		mv.addObject("userInfo",userInfo);
		mv.setViewName("skillmap");
		
		return mv;
	}
	
	
}
