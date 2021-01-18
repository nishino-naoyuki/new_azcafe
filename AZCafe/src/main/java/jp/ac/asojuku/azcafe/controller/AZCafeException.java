package jp.ac.asojuku.azcafe.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AZCafeException extends Exception {

	Logger logger = LoggerFactory.getLogger(AZCafeException.class);

	public AZCafeException(String errMsg){
		logger.error("致命的エラー：",errMsg);
	}
	public AZCafeException(Exception e) {
		logger.error("致命的エラー：",e);
	}
}
