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

import jp.ac.asojuku.azcafe.dto.AssignmentDetailDto;
import jp.ac.asojuku.azcafe.dto.AssignmentDto;
import jp.ac.asojuku.azcafe.dto.AssignmentElementDto;
import jp.ac.asojuku.azcafe.dto.CommentInsertDto;
import jp.ac.asojuku.azcafe.dto.GradingResultDetailDto;
import jp.ac.asojuku.azcafe.dto.GroupDto;
import jp.ac.asojuku.azcafe.dto.HomeroomDto;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.exception.AZCafePermissonDeniedException;
import jp.ac.asojuku.azcafe.form.AssignmentForm;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.repository.AnswerGoodRepository;
import jp.ac.asojuku.azcafe.service.AnswerService;
import jp.ac.asojuku.azcafe.service.AssignmentService;
import jp.ac.asojuku.azcafe.service.CommentServie;
import jp.ac.asojuku.azcafe.service.GroupService;
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
	AnswerService answerService;
	@Autowired
	HttpSession session;
	@Autowired
	GroupService groupService;
	@Autowired
	CommentServie commentServie;

	@RequestMapping(value= {"/edit"}, method=RequestMethod.GET)
	public ModelAndView edit(
			AssignmentForm assignmentForm,
			@RequestParam(required = false) Integer assignmentId,
			ModelAndView mv) {
		
		AssignmentDto assignmentDto =  assignmentService.get(assignmentId);

		//課題グループを取得する
		List<GroupDto> groupList = groupService.getAll();
		mv.addObject("groupList",groupList);
		//dto->form
		getFormFromDto(assignmentForm,assignmentDto);
		assignmentForm.setAssignmentId(assignmentId);
		
		List<HomeroomDto> list = homeroomService.getAll();			
		mv.addObject("homeroomList", list);

        mv.setViewName("assignment_create");
        return mv;
	}
	/**
	 * イイネを登録する
	 * @param answerId
	 * @return
	 * @throws AZCafeException
	 */
	@ResponseBody
	@RequestMapping(value= {"/good"}, method=RequestMethod.POST)
	public Object good(@RequestParam(required = false) Integer answerId) throws AZCafeException {
		if( answerId == null ) {
			return "NG:パラメータが不正です。";
		}

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		
		int goodNum = assignmentService.insertGood(answerId, loginInfo.getUserId());
		
		return "OK:"+goodNum;
	}
	/**
	 * メッセージ送信する
	 * @param mv
	 * @param message
	 * @param answerId
	 * @param assignmentId
	 * @return
	 * @throws AZCafeException
	 */
	@RequestMapping(value= {"/sendMsg"}, method=RequestMethod.POST)
	public ModelAndView sendMessage(
			ModelAndView mv,
			@RequestParam(required = false) String message,
			@RequestParam(required=false) Integer answerUserId,
			@RequestParam(required=false) Integer answerId,
			@RequestParam(required = false) Integer assignmentId) throws AZCafeException {

		CommentInsertDto commentInsertDto = new CommentInsertDto();

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		
		commentInsertDto.setAnswerUserId(answerUserId);
		commentInsertDto.setUserId(loginInfo.getUserId());
		commentInsertDto.setAnswerId(answerId);
		commentInsertDto.setAssignmentId(assignmentId);
		commentInsertDto.setMessage(message);
		
		GradingResultDetailDto gradingResultDetailDto =
				commentServie.insert(commentInsertDto);
		
		mv.addObject("gradingResultDetailDto", gradingResultDetailDto);
        mv.setViewName("assignment_result");
        return mv;
	}

	/**
	 * 採点結果の取得
	 * 
	 * @param mv
	 * @param assignmentId
	 * @return
	 * @throws AZCafeException
	 * @throws AZCafePermissonDeniedException 
	 */
	@RequestMapping(value= {"/result"}, method=RequestMethod.GET)
	public ModelAndView result(
			ModelAndView mv,
			@RequestParam(required = false) Integer assignmentId,
			@RequestParam(required = false) Integer userId) throws AZCafeException, AZCafePermissonDeniedException {

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		
		if( userId == null ) {
			userId = loginInfo.getUserId();
		}else {
			//ユーザーが情報を見れるかをチェックする
			if( !answerService.isWatch(assignmentId, userId, loginInfo.getUserId()) ) {
				throw new AZCafePermissonDeniedException("この画面を見る権限がありません");
			}
		}
		
		//詳細情報を取得する
		GradingResultDetailDto gradingResultDetailDto = 
												answerService.getBy(userId, assignmentId);
		
		mv.addObject("gradingResultDetailDto", gradingResultDetailDto);
        mv.setViewName("assignment_result");
        return mv;
	}

	/**
	 * 課題一覧
	 * 
	 * @param mv
	 * @return
	 * @throws AZCafeException
	 */
	@RequestMapping(value= {"/list"}, method=RequestMethod.GET)
	public ModelAndView list(ModelAndView mv) throws AZCafeException {

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		//一覧取得
		List<AssignmentElementDto> assignmentList = assignmentService.getAll(loginInfo.getUserId());
		
		mv.addObject("assignmentList",assignmentList);
		
        mv.setViewName("assignment_list");
        return mv;
	}
	
	/**
	 * 詳細情報を取得する
	 * @param mv
	 * @param id
	 * @return
	 * @throws AZCafeException 
	 */
	@RequestMapping(value= {"/detail"}, method=RequestMethod.GET)
	public ModelAndView detail(
			ModelAndView mv,@RequestParam(required = false) Integer id) throws AZCafeException {

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		
		AssignmentDetailDto assignmentdetailDto = assignmentService.getDetail(id,loginInfo.getUserId());
		
		mv.addObject("assignmentdetailDto",assignmentdetailDto);
		mv.addObject("assignmentId",id);
		
        mv.setViewName("assignment_detail");
        return mv;
	}
	
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

		//課題グループを取得する
		List<GroupDto> groupList = groupService.getAll();
		mv.addObject("groupList",groupList);
		
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
	public String insertOrUpdate() {
		//セッションから登録情報を取得する
		AssignmentDto dto = (AssignmentDto)session.getAttribute(SessionConst.ASSIGNMENTINFO);
		if( dto == null ) {
			return "登録に失敗しました";
		}
		
		assignmentService.insertOrUpdate(dto);
		
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
		
		dto.setAssignmentId(assignmentForm.getAssignmentId());
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
	private AssignmentForm getFormFromSession(AssignmentForm form) {
		
		AssignmentDto dto = (AssignmentDto)session.getAttribute(SessionConst.ASSIGNMENTINFO);
		if( dto != null ) {
			getFormFromDto(form,dto);
		}
		
		return form;
	}
	
	private AssignmentForm getFormFromDto(AssignmentForm form,AssignmentDto dto) {
		if(form == null ) {
			form = new AssignmentForm();
		}

		form.setAssignmentId(dto.getAssignmentId());
		form.setGroup(dto.getGroup());
		form.setTitle(dto.getTitle());
		form.setDifficulty(dto.getDifficultyAsInt());
		form.setAnswerList(dto.getAnswerList());
		form.setPublicStateList(dto.getPublicStateList());
		form.setContent(dto.getContent());
		
		return form;
	}
}
