package jp.ac.asojuku.azcafe.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.azcafe.dto.AssignmentDto;
import jp.ac.asojuku.azcafe.dto.HomeroomDto;
import jp.ac.asojuku.azcafe.form.AssignmentForm;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.AssignmentService;
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
	@Autowired
	AssignmentService assignmentService;
	@Autowired
	HttpSession session;
	
	/**
	 * 問題作成のメソッド
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/create"}, method=RequestMethod.GET)
	public ModelAndView create(
			AssignmentForm assignmentForm,
			ModelAndView mv,
			@RequestParam(required = false) Integer returnFlg) {

		if( returnFlg != null && returnFlg == 1) {
			//確認画面からの戻り
			getFormFromSession(assignmentForm);
		}else {
			//初期化（左メニューからの表示）
			List<HomeroomDto> list = homeroomService.getAll();			
			mv.addObject("homeroomList", list);
			assignmentForm.initPublicStateList(list); 
		}
		
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
			//クラス一覧取得
			List<HomeroomDto> list = homeroomService.getAll();
			mv.addObject("homeroomList", list);
	        mv.setViewName("assignment_create");
		}else {
			//セッションに保存して画面遷移
			AssignmentDto dto = getDtoFrom(assignmentForm);
			session.setAttribute(SessionConst.ASSIGNMENTINFO, dto);
			
			//画面表示用にセットする
			mv.addObject("assignmentDto",dto);
	        mv.setViewName("assignment_confirm");
		}

        return mv;
	}
	
	@RequestMapping(value= {"/insert"}, method=RequestMethod.POST)
	@ResponseBody
	public String insert() {
		//セッションから登録情報を取得する
		AssignmentDto dto = (AssignmentDto)session.getAttribute(SessionConst.ASSIGNMENTINFO);
		if( dto == null ) {
			return "登録に失敗しました";
		}
		
		assignmentService.insert(dto);
		
		//セッション削除
		session.removeAttribute(SessionConst.ASSIGNMENTINFO);
		
		return "登録完了しました";
	}
	
	/**
	 * Form→DTO変換
	 * @param assignmentForm
	 * @return
	 */
	private AssignmentDto getDtoFrom(AssignmentForm assignmentForm) {
		AssignmentDto dto = new AssignmentDto();
		
		dto.setGroup(assignmentForm.getGroup());
		dto.setTitle(assignmentForm.getTitle());
		dto.setDifficulty(assignmentForm.getDifficulty());
		dto.setContent(assignmentForm.getContent());
		dto.setAnswerList(assignmentForm.getAnswerList());
		dto.setPublicStateList(assignmentForm.getPublicStateList());
		
		return dto;
	}
	
	/**
	 * セッションの情報からForm情報を復元する
	 * @param from
	 * @return
	 */
	private AssignmentForm getFormFromSession(AssignmentForm from) {
		
		AssignmentDto dto = (AssignmentDto)session.getAttribute(SessionConst.ASSIGNMENTINFO);
		if( dto != null ) {
			from.setGroup(dto.getGroup());
			from.setTitle(dto.getTitle());
			from.setDifficulty(dto.getDifficultyAsInt());
			from.setAnswerList(dto.getAnswerList());
			from.setPublicStateList(dto.getPublicStateList());
			from.setContent(dto.getContent());
		}
		
		return from;
	}
}
