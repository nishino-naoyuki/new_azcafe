package jp.ac.asojuku.azcafe.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.repository.AssignmentRepository;
import jp.ac.asojuku.azcafe.service.grading.GradingFactory;
import jp.ac.asojuku.azcafe.service.grading.GradingProcess;

/**
 * 採点サービス
 * @author nishino
 *
 */
@Service
public class GradingService {
	
	@Autowired
	AssignmentRepository assignmentRepository;

	/**
	 * @param userId
	 * @param lang
	 * @param code
	 * @throws AZCafeException
	 */
	public void execByText(
			Integer userId,
			Integer assignmentId,
			Language lang,
			String code) throws AZCafeException {

		GradingProcess gp = GradingFactory.getGradingProcess(lang);
		AssignmentTblEntity entity = assignmentRepository.getOne(assignmentId);
		
		//処理を実行する
		gp.execByText(entity,userId, code);
		
	}
	
}
