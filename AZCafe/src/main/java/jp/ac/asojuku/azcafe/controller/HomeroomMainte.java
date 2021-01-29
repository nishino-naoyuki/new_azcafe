package jp.ac.asojuku.azcafe.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.form.HomeroomInputForm;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.HomeroomService;
import jp.ac.asojuku.azcafe.service.UserService;

@RestController
public class HomeroomMainte {
	
	@Autowired
	HomeroomService homeroomService;
	@Autowired
	UserService userService;
	@Autowired
	HttpSession session;

	/**
	 * クラス名の更新
	 * @param homeroomInputForm
	 * @param err
	 * @return
	 */
	@RequestMapping(value= {"/homeroom/update"}, method=RequestMethod.POST)
	public Object update(
			@Valid HomeroomInputForm homeroomInputForm,BindingResult err
			) {
		
		//クラス情報を更新
		homeroomService.update(homeroomInputForm.getId(), homeroomInputForm.getName());
		
		//ログイン情報を更新
		updateLoginInfo();		
		
		return "ok";
	}
	
	/**
	 * セッションのログイン情報を更新する
	 */
	public void updateLoginInfo() {
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		loginInfo = userService.getLoginInfo(loginInfo.getUserId());
		
		session.setAttribute(SessionConst.LOGININFO, loginInfo);
	}
}
