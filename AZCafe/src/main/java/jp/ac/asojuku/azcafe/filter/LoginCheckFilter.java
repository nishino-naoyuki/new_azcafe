package jp.ac.asojuku.azcafe.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.param.SessionConst;

@Component
public class LoginCheckFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(LoginCheckFilter.class);

	//チェック除外画面
	private String excludeDispList[] = null;

	@Autowired
	HttpSession session;
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		try {
//			excludeDispList = 
//					AZCafeConfig.getInstance().getNologindisplay().split(",");
//		} catch (Exception e) {
//			logger.error("設定ファイルの読み込みエラー:getNoLoginDisplay");
//		}
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if( excludeDispList == null ) {
			initExcludeInfo();
		}
		//リクエストのサーブレットパスを取得
		String servletPath = ((HttpServletRequest)request).getServletPath();
		logger.trace("servletPath:"+servletPath);
		
		//除外対象かどうかをチェック
		if( isExclude(servletPath )) {
			logger.trace("exclude!");
			chain.doFilter(request, response);
			return;
		}
		
		//セッションからユーザー情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		if( loginInfo == null ) {
			//ログイン画面へ転送
			logger.info("Filter!!! servletPath="+servletPath);
			((HttpServletResponse)response).sendRedirect( "/");
			return;
		}
		
		chain.doFilter(request, response);

	}
	
	private void initExcludeInfo() {
		try {
			excludeDispList = 
					AZCafeConfig.getInstance().getNologindisplay().split(",");
		} catch (Exception e) {
			logger.error("設定ファイルの読み込みエラー:getNoLoginDisplay");
		}
	}
	
	/**
	 * 除外URLかどうかの判定を行う
	 * 
	 * @param servletPath
	 * @return
	 */
	private boolean isExclude(String servletPath) {
		boolean ret = false;
		
		for( String exclude :excludeDispList ) {
			if(servletPath.matches(exclude)) {
				ret = true;
				break;
			}
		}
		
		return ret;
	}

}
