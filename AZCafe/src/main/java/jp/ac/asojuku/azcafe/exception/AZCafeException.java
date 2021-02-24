package jp.ac.asojuku.azcafe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AZCafeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(AZCafeException.class);

	public AZCafeException(String errMsg){
		logger.error("致命的エラー：",errMsg);
	}
	public AZCafeException(Exception e) {
		logger.error("致命的エラー：",e);
	}
}
