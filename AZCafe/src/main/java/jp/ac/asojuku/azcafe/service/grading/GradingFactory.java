package jp.ac.asojuku.azcafe.service.grading;

import jp.ac.asojuku.azcafe.param.Language;

public class GradingFactory {

	/**
	 * 処理クラスを取得する
	 * 
	 * @param lang
	 * @return
	 */
	public static GradingProcess getGradingProcess(Language lang) {
		GradingProcess inst = null;
		
		if( lang == Language.JAVA ) {
			inst = new GradingJava(lang);
		}else if( lang == Language.JAVASCRIPT ) {
			inst = new GradingJavaScript(lang);
		}else {
			inst = new GradingJava(lang);	//TODO
		}
		
		return inst;
	}
}
