package jp.ac.asojuku.azcafe.service;


import org.springframework.stereotype.Service;

import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.service.grading.GradingFactory;
import jp.ac.asojuku.azcafe.service.grading.GradingProcess;

/**
 * 採点サービス
 * @author nishino
 *
 */
@Service
public class GradingService {

	/**
	 * @param userId
	 * @param lang
	 * @param code
	 * @throws AZCafeException
	 */
	public void execByText(Integer userId,Language lang,String code) throws AZCafeException {

		GradingProcess gp = GradingFactory.getGradingProcess(lang);
		
		//処理を実行する
		gp.execByText(userId, code);
		
	}
	
}
