/**
 * 
 */
package jp.ac.asojuku.azcafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;

/**
 * @author nishino
 * ログインコントローラ
 */
@Controller
public class LoginController {
	
	@Autowired
	AZCafeConfig config;

	@RequestMapping(value= {"/login"}, method=RequestMethod.GET)
    public ModelAndView login(ModelAndView mv) {
		
		mv.setViewName("login");
		return mv;
	}
    		
}
