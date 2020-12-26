/**
 * 
 */
package jp.ac.asojuku.azcafe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author nishino
 * ダッシュボード画面
 */
public class DashboadController {

	@RequestMapping(value= {"/","/dashboad"}, method=RequestMethod.GET)
    public ModelAndView login(ModelAndView mv) {
		
		mv.setViewName("login");
		return mv;
	}
}
