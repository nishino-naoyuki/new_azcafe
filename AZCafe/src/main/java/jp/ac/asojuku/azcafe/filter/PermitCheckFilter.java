package jp.ac.asojuku.azcafe.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.param.RoleId;
import jp.ac.asojuku.azcafe.param.SessionConst;


@Component
public class PermitCheckFilter  implements Filter{
	private static final Logger logger = LoggerFactory.getLogger(PermitCheckFilter.class);

	@Autowired
	HttpSession session;
	
	//管理者のみアクセス可能
//	String[] permitMangerOnly = {
//			"/user","/room/input","/room/confirm","/room/insert"
//	};
	
	//学生がアクセス不可のURLを正規表現で記載
	String[] sudentDeniedList;
/*	= {
			"^/pwd/(reset_input|reset)$",	//パスワードリセット
			"^/room/(?!disp)",	//ルームのdisp以外
			"^/bbs/(?!detail)",	//掲示板は詳細画面のみ
			"^/master.*"
	};*/

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		try {
//			sudentDeniedList = 
//					AppSettingProperty.getInstance().getStudentDenied();
//		} catch (AsoBbsSystemErrException e) {
//			logger.error("設定ファイルの読み込みエラー:getStudentDenied");
//		}
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		//リクエストのサーブレットパスを取得
		String servletPath = ((HttpServletRequest)request).getServletPath();
		//セッションからユーザー情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		if( loginInfo == null ) {
			chain.doFilter(request, response);
			return;
		}

		if( sudentDeniedList == null ) {
			initSudentDeniedList();
		}
		
		logger.trace("PermitCheckFilter:"+servletPath);
		
		//管理者のみ
//		if( loginInfo.getRole() == RoleId.TEACHER ) {
//			if( Arrays.asList(permitMangerOnly).contains(servletPath) ) {
//				//アクセス不可能
//				logger.warn("Filter!!! [permit deny1] mail= "+loginInfo.getMail()+" url="+servletPath);
//				((HttpServletResponse)response).sendRedirect("/error/accessdeny");
//				return;
//			}
//		}

		//管理者・教員のみ
		if( loginInfo.getRole() == RoleId.STUDENT ) {
			if( isDeniedStudent(servletPath) ) {
				//アクセス不可能
				logger.warn("Filter!!! [permit deny2] mail= "+loginInfo.getMail()+" url="+servletPath);
				//あえて４０４の画面へ遷移（「権限がない」ページだと存在することがばれる）
				((HttpServletResponse)response).sendRedirect("/error/404");
				return;
			}
		}
		
		chain.doFilter(request, response);
		
	}

	private void initSudentDeniedList() {
		try {
			sudentDeniedList = 
					AZCafeConfig.getInstance().getStudentdenied().split(",");
		} catch (Exception e) {
			logger.error("設定ファイルの読み込みエラー:getNoLoginDisplay");
		}
	}
	/**
	 * 学生権限でアクセス不可能なページかどうかを判定する
	 * 
	 * @param servletPath
	 * @return true:アクセス不可能　false：アクセス可能
	 */
	private boolean isDeniedStudent(String servletPath) {
		boolean bDenid = false;
		//sudentDeniedListにあるパス以下のURLはアクセスNGとする
		for(String deniedPtn: sudentDeniedList) {
			Pattern p = Pattern.compile(deniedPtn);
			Matcher m = p.matcher(servletPath);
			if( m.find()) {
				bDenid = true;
				break;
			}
		}
		
		return bDenid;
	}

}
