package jp.ac.asojuku.azcafe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AZCafePermissonDeniedException extends Exception{

	Logger logger = LoggerFactory.getLogger(AZCafePermissonDeniedException.class);
	
	public AZCafePermissonDeniedException(String errMsg){
		logger.error("権限なしエラー：",errMsg);
	}
	public AZCafePermissonDeniedException(Exception e) {
		logger.error("権限なしエラー：",e);
	}
}
