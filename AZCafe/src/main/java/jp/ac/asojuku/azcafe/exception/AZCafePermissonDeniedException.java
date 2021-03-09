package jp.ac.asojuku.azcafe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AZCafePermissonDeniedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3377186599045922339L;
	Logger logger = LoggerFactory.getLogger(AZCafePermissonDeniedException.class);
	
	public AZCafePermissonDeniedException(String errMsg){
		logger.error("権限なしエラー：",errMsg);
	}
	public AZCafePermissonDeniedException(Exception e) {
		logger.error("権限なしエラー：",e);
	}
}
