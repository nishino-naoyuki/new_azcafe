package jp.ac.asojuku.azcafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 管理コントローラ
 * @author nishino
 *
 */
@Controller
public class ManagementController {

	@RequestMapping(value= {"/management"}, method=RequestMethod.GET)
	public ModelAndView manage(ModelAndView mv) {
		 mv.setViewName("management");
		 return mv;
	}
}
