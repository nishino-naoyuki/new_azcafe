package jp.ac.asojuku.azcafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

	@RequestMapping(value= {"/dashboad"}, method=RequestMethod.GET)
    public ModelAndView index(@ModelAttribute("msg")String msg,ModelAndView mv) {

		//ログイン情報を取得する
//		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		//1週間以内の更新情報を取得する
//		DashBoadDto dashBoadDto = bbsService.getRecentlyBbs(loginInfo);
		
//		mv.addObject("dashBoadDto",dashBoadDto);
        mv.setViewName("/dashboad");
        return mv;
    }
}
