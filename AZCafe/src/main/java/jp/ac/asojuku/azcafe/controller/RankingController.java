package jp.ac.asojuku.azcafe.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.azcafe.dto.HomeroomDto;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.dto.RankingDto;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.HomeroomService;
import jp.ac.asojuku.azcafe.service.UserService;

@Controller
@RequestMapping(value= {"/ranking"})
public class RankingController {

	private final int ALL = -1;
	@Autowired
	HomeroomService homeroomService;
	@Autowired
	UserService userService;
	@Autowired
	HttpSession session;

	/**
	 * セレクトボックスでクラスが変更されたときの処理
	 * 
	 * @param mv
	 * @param homeroomId
	 * @return
	 * @throws AZCafeException
	 */
	@RequestMapping(value= {"/disp"}, method=RequestMethod.POST)
	public ModelAndView change(ModelAndView mv,@RequestParam Integer homeroomId) throws AZCafeException {

		//クラス一覧取得
		List<HomeroomDto> list = homeroomService.getAll();
		//	ランキング（デフォルトではログインしているユーザーのクラス）
		List<RankingDto> rankingList = userService.getRanking(( homeroomId == ALL ? null : homeroomId));

		mv.addObject("selHomeroomId",homeroomId);
		mv.addObject("homeroomList",list);
		mv.addObject("rankingList",rankingList);
		mv.setViewName("ranking");
		return mv;
	}
	
	/**
	 * メニューからランキングがクリックされたとき
	 * デフォルトで自クラスのランキングを表示する
	 * 
	 * @param mv
	 * @return
	 * @throws AZCafeException
	 */
	@RequestMapping(value= {"/disp"}, method=RequestMethod.GET)
	public ModelAndView disp(ModelAndView mv) throws AZCafeException {

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			throw new AZCafeException("不正なアクセスです");
		}
		
		//クラス一覧取得
		List<HomeroomDto> list = homeroomService.getAll();
		//	ランキング（デフォルトではログインしているユーザーのクラス）
		List<RankingDto> rankingList = userService.getRanking(loginInfo.getHomeroomId());

		mv.addObject("selHomeroomId",loginInfo.getHomeroomId());
		mv.addObject("homeroomList",list);
		mv.addObject("rankingList",rankingList);
		mv.setViewName("ranking");
		return mv;
	}
}
