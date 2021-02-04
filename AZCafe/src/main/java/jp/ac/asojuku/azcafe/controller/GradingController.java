package jp.ac.asojuku.azcafe.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.form.GradingByFileForm;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.GradingService;

/**
 * 採点コントローラ
 * @author nishino
 *
 */
@RestController
@RequestMapping(value= {"/grading"})
public class GradingController {
	
	@Autowired
	GradingService gradingService;
	
	@Autowired
	HttpSession session;

	@RequestMapping(value= {"/file"}, method=RequestMethod.POST)
	public Object gradingByFile(
			@Valid GradingByFileForm form) throws AZCafeException {
		
		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		
		//採点実行
		gradingService.execByText(
				loginInfo.getUserId(), 
				Language.getBy(form.getLanguage()), 
				form.getAnswerText());
		
		return "ok";
	}
}
