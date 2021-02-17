package jp.ac.asojuku.azcafe.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.azcafe.dto.DashBoadDto;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.DashboadService;

@Controller
public class DashboardController {
	@Autowired
	DashboadService dashboadService;
	@Autowired
	HttpSession session;
	
	/**
	 * ダッシュボード表示
	 * @param msg
	 * @param mv
	 * @return
	 * @throws AZCafeException
	 */
	@RequestMapping(value= {"/dashboad"}, method=RequestMethod.GET)
    public ModelAndView index(@ModelAttribute("msg")String msg,ModelAndView mv) throws AZCafeException {

		//ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		//1週間以内の更新情報を取得する
		DashBoadDto dashBoadDto = dashboadService.getData(loginInfo.getUserId());
		
		mv.addObject("dashBoadDto",dashBoadDto);
        mv.setViewName("/dashboad");
        return mv;
    }
}
