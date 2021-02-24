package jp.ac.asojuku.azcafe.controller;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.exception.AZCafePermissonDeniedException;

/**
 * コントローラーに共通する例外発生時の処理
 * @author nishino
 *
 */
@ControllerAdvice
public class WebExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(WebExceptionHandler.class);

	/**
	 * システムエラー画面
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({AZCafeException.class})
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception exception) {
		logger.error("AZCafeException", exception);

		return "/error/systemerror";	// error1.htmlへ遷移
	}

	/**
	 * 権限拒否画面
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({AZCafePermissonDeniedException.class})
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public String handleAccessDenied(Exception exception) {
		logger.error("AZCafePermissonDeniedException", exception);

		return "/error/accessdeny";	// error1.htmlへ遷移
	}
	
	/**
	 * 404エラー
	 * @param exception
	 * @return
	 */
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String notFound(Exception exception) {
		logger.error("NOT_FOUND", exception);

		return "/error/404";	// error1.htmlへ遷移
	}
	
	/**
	 * 例外全般
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(Throwable.class)
    public String ThrowableHandler(Exception exception) {
		logger.error("ThrowableHandler", exception);
		return "/error/systemerror";	// error1.htmlへ遷移
    }
	

	/**
	 * ファイルサイズ
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(FileSizeLimitExceededException.class)
	public String fileSizeException(Exception exception) {
		logger.error("fileSizeException", exception);

		return "/error/systemerror";	// error1.htmlへ遷移
	}
}
