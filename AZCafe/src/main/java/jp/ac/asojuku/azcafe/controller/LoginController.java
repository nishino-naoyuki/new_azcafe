/**
 * 
 */
package jp.ac.asojuku.azcafe.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.config.MessageProperty;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.UserService;
import jp.ac.asojuku.form.LoginForm;

/**
 * @author nishino
 * ログインコントローラ
 */
@Controller
public class LoginController {

	@Autowired
	AZCafeConfig cofing;
	@Autowired
	HttpSession session;
	@Autowired
	UserService userService;

	@RequestMapping(value= {"/login"}, method=RequestMethod.GET)
    public ModelAndView login(
    		ModelAndView mv,
    		@ModelAttribute("msg")String msg,
    		@ModelAttribute("mail")String mail
    		) {
		

        //エラーメッセージがあればメッセージを仕込んでおく
        if( msg != null && msg.length() > 0) {
        	mv.addObject("msg", msg);
        }else {
        	mv.addObject("msg", "");
        }
        LoginForm form = new LoginForm();
        form.setMail(mail);
    	mv.addObject("loginForm", form);
        mv.setViewName("login");
        
		return mv;
	}

	/**
	 * ログインエラー時の処理
	 * 
	 * @param redirectAttributes
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	private String fowardLoginError(RedirectAttributes redirectAttributes,LoginForm form) throws AZCafeException {
		//エラーメッセージを取得
		String errMsg = MessageProperty.getInstance().getProperty(MessageProperty.LOGIN_ERR_LOGINERR);
		//エラーメッセージをセット
		redirectAttributes.addFlashAttribute("msg", errMsg);
		redirectAttributes.addFlashAttribute("mail", form.getMail());
		
		return "redirect:login";
	}
    		

	/**
	 * @param redirectAttributes
	 * @param form
	 * @param mv
	 * @param request
	 * @param response
	 * @return
	 * @throws AZCafeException
	 */
	@RequestMapping(value= {"/auth"}, method=RequestMethod.POST)
	public String auth(
			RedirectAttributes redirectAttributes,
			LoginForm form,
    		ModelAndView mv,
    		HttpServletRequest request,
    		HttpServletResponse response
			) throws AZCafeException {
		String url;
		
		url = "redirect:dashboad";
		
		//認証
		LoginInfoDto loginInfo = userService.login(form.getMail(), form.getPassword());
		if( loginInfo != null) {
			//トークンを発行して、クッキーに保存
			//setTokenCookie(loginInfo,request,response);
			//セッションにログイン情報を保存
			session.setAttribute(SessionConst.LOGININFO,loginInfo);
			url = "redirect:dashboad";
		}else {
			url = fowardLoginError(redirectAttributes,form);
		}
		return url;
	}
}
