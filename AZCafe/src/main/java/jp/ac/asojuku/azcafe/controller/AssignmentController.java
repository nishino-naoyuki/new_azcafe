package jp.ac.asojuku.azcafe.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.azcafe.dto.HomeroomDto;
import jp.ac.asojuku.azcafe.form.AssignmentForm;
import jp.ac.asojuku.azcafe.service.HomeroomService;

/**
 * 課題コントローラ
 * @author nishino
 *
 */
@Controller
@RequestMapping(value= {"/assignment"})
public class AssignmentController {

	@Autowired
	HomeroomService homeroomService;
	
	/**
	 * 問題作成のメソッド
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/create"}, method=RequestMethod.GET)
	public ModelAndView create(AssignmentForm assignmentForm,ModelAndView mv) {

		//クラス一覧取得
		List<HomeroomDto> list = homeroomService.getAll();
		
		mv.addObject("homeroomList", list);
		
        mv.setViewName("assignment_create");
        return mv;
	}
	
	/**
	 * 確認画面
	 * 
	 * @param assignmentForm
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/confirm"}, method=RequestMethod.POST)
	public ModelAndView confirm(
			@Valid AssignmentForm assignmentForm,BindingResult bindingResult,
			ModelAndView mv) {
		
		if( bindingResult.hasErrors()) {
			//エラーがある場合
	        mv.setViewName("assignment_create");
		}else {
	        mv.setViewName("assignment_confirm");
		}

        return mv;
	}
}
