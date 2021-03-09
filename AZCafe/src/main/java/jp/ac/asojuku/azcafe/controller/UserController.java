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

import jp.ac.asojuku.azcafe.config.ValidationConfig;
import jp.ac.asojuku.azcafe.dto.HomeroomDto;
import jp.ac.asojuku.azcafe.dto.LevelDto;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.dto.UserInfoDto;
import jp.ac.asojuku.azcafe.dto.UserSearchElementDto;
import jp.ac.asojuku.azcafe.err.ErrorCode;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.exception.AZCafePermissonDeniedException;
import jp.ac.asojuku.azcafe.form.UpdateIconForm;
import jp.ac.asojuku.azcafe.form.UserSearchConditionForm;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.HomeroomService;
import jp.ac.asojuku.azcafe.service.LevelService;
import jp.ac.asojuku.azcafe.service.UserService;
import jp.ac.asojuku.azcafe.util.FileUtils;
import jp.ac.asojuku.validator.UserValidator;

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

	/**
	 * アイコンを更新する
	 * @param mv
	 * @param updateIconForm
	 * @return
	 * @throws AZCafeException
	 */
	@RequestMapping(value= {"/update/icon"}, method=RequestMethod.POST)
	public ModelAndView updateIcon(ModelAndView mv,@Valid UpdateIconForm updateIconForm) throws AZCafeException {
				
		//アイコンファイルをアップロード
		String iconPath = FileUtils.uploadIconFile( updateIconForm.getIcon() );

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			throw new AZCafeException("不正なアクセスです");
		}
		
		userService.updateIcon(loginInfo.getUserId(), iconPath);

		//セッションの中の情報も更新する
		loginInfo.setAvater(iconPath);
		session.setAttribute(SessionConst.LOGININFO, loginInfo);
		
		//ログインユーザーの情報を再取得する
		UserInfoDto userInfoDto = userService.getInfo(
				loginInfo.getUserId(),
				loginInfo.getUserId());
		
		mv.addObject("userInfoDto",userInfoDto);
		mv.setViewName("user_info");
		return mv;
	}

	/**
	 * パスワードリセット画面の表示
	 * 
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/reset/password"}, method=RequestMethod.GET)
	public ModelAndView passwordResetDisp(ModelAndView mv) {
		mv.setViewName("password_reset");
		return mv;
	}
	/**
	 * パスワードのリセット
	 * @param userId
	 * @param password1
	 * @param password2
	 * @return
	 * @throws AZCafeException
	 */
	@RequestMapping(value= {"/reset/password"}, method=RequestMethod.POST)
	@ResponseBody
	public Object passwordReset(
			String mail,
			String password1,
			String password2 ) throws AZCafeException {
		
		//メアドの存在チェック
		if( !userService.isExistMailadress(mail) ) {
			return ValidationConfig.getInstance().getMsg(ErrorCode.ERR_PWD_CHG_MAIL_NOTEXIST);
		}
		
		String errMsg =  validatePassword(password1,password2);
	
		userService.updatePassword(mail, password1);
		
		return errMsg;
	}
	/**
	 * パスワード変更
	 * @param userId
	 * @param password1
	 * @param password2
	 * @return
	 * @throws AZCafePermissonDeniedException 
	 * @throws AZCafeException 
	 */
	@RequestMapping(value= {"/update/password"}, method=RequestMethod.POST)
	@ResponseBody
	public Object updatePassword(
			Integer userId,
			String password1,
			String password2 ) throws AZCafePermissonDeniedException, AZCafeException {

		//IDが自分のものをかをチェックする
		isIdMyself(userId);
		
		String errMsg =  validatePassword(password1,password2);
	
		userService.updatePassword(userId, password1);
		
		return errMsg;
	}
	
	/**
	 * ニックネームの更新
	 * 
	 * @param userId
	 * @param nickName
	 * @return
	 * @throws AZCafeException
	 * @throws AZCafePermissonDeniedException 
	 */
	@RequestMapping(value= {"/update/nickname"}, method=RequestMethod.POST)
	@ResponseBody
	public Object updateNickname(
			@RequestParam(required = false) Integer userId,
			@RequestParam(required = false) String nickName) throws  AZCafePermissonDeniedException, AZCafeException {

		//IDが自分のものをかをチェックする
		isIdMyself(userId);
		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		String errMsg = "ok";
		if( UserValidator.useNickName(nickName) != true ) {
			errMsg = ValidationConfig.getInstance().getMsg(ErrorCode.ERR_MEMBER_ENTRY_NICKNAME);
		}
		
		userService.updateNickName(userId, nickName);
		
		//セッションの中の情報も更新する
		loginInfo.setNickName(nickName);
		session.setAttribute(SessionConst.LOGININFO, loginInfo);
		
		return errMsg;
	}
	/**
	 * 検索画面を表示する
	 * 
	 * @param mv
	 * @return
	 */
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
	
	/**
	 * ユーザー情報の検索を行う
	 * 検索結果はJSONで返す
	 * 
	 * @param cond
	 * @return
	 * @throws JsonProcessingException
	 */
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

	/**
	 * ログインユーザーが指定したユーザーをフォローする
	 * 
	 * @param userId
	 * @return
	 * @throws AZCafeException
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value= {"/follow"}, method=RequestMethod.POST)
	@ResponseBody
	public Object follow(@RequestParam(required = false) Integer userId) throws AZCafeException, JsonProcessingException {

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		
		userService.insertFollow(loginInfo.getUserId(), userId);

		//ログインユーザー情報を取得する
		UserInfoDto userInfoDto = userService.getInfo(
				loginInfo.getUserId(),
				loginInfo.getUserId());
		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(userInfoDto);
		
        logger.trace("jsonString:{}",jsonString);
        
		return jsonString;
	}

	/**
	 * ログインユーザーが指定されたユーザーのフォローを解除する
	 * 
	 * @param userId
	 * @return
	 * @throws AZCafeException
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value= {"/removeFollow"}, method=RequestMethod.POST)
	@ResponseBody
	public Object removeFollow(@RequestParam(required = false) Integer userId) throws AZCafeException, JsonProcessingException {

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		
		userService.removeFollow(loginInfo.getUserId(), userId);

		//ログインユーザー情報を取得する
		UserInfoDto userInfoDto = userService.getInfo(
				loginInfo.getUserId(),
				loginInfo.getUserId());
		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(userInfoDto);
		
        logger.trace("jsonString:{}",jsonString);
        
		return jsonString;
	}

	
	/**
	 * 指定されたユーザーの情報を表示する
	 * 
	 * @param mv
	 * @param userId
	 * @return
	 * @throws AZCafeException
	 */
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

	private void isIdMyself(Integer userId) throws AZCafePermissonDeniedException {
		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null || loginInfo.getUserId() != userId) {
			throw new AZCafePermissonDeniedException("不正なアクセスです");
		}
	}
	/**
	 * パスワード変更変更処理
	 * 
	 * @param userId パスワードを変更する対象のID
	 * @param password1
	 * @param password2
	 * @return
	 * @throws AZCafeException
	 */
	private String validatePassword(
			String password1,
			String password2 ) throws AZCafeException {

		String errMsg ="ok";
		//パスワード
		if( password1.equals(password2) != true) {
			//パスワード不一致
			errMsg = ValidationConfig.getInstance().getMsg(ErrorCode.ERR_MEMBER_ENTRY_PASSWORD_NOTMATCH);
		}
		if( UserValidator.password(password1) != true) {
			//パスワードポリシーエラー
			errMsg = ValidationConfig.getInstance().getMsg(ErrorCode.ERR_MEMBER_ENTRY_PASSWORD_POLICY);
		}

		
		return errMsg;
	}
}
