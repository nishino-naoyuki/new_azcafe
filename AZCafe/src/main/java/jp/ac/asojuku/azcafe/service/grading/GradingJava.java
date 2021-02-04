package jp.ac.asojuku.azcafe.service.grading;


import java.io.IOException;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.util.FileUtils;


public class GradingJava extends GradingProcess {

	public GradingJava(Language lang) {
		super(lang);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public ProcessBuilder execBatch(String batchFName,String workDir,String code) throws AZCafeException {

		//プログラムをファイルに出力する
		String className = getClassName(code);
		String srcFileName = getCodeFileName(className);
		FileUtils.outputFile(workDir, srcFileName, code);
		
		makeDir(workDir);
		//バッチを実行する
		ProcessBuilder pb = new ProcessBuilder(batchFName,workDir,workDir,className);
		
		return pb;
	}
	
	public void makeDir(String workDir) {
		FileUtils.makeDir(workDir+"/classes");
	}
	/**
	 * ソースコードのファイルを名を取得する
	 * @param lang
	 * @param code
	 * @return
	 */
	public String getCodeFileName(String className) {
		
		String fileName = "";
		fileName = className+ ".java";
		
		return fileName;
	}
	
	/**
	 * ソースコードからクラス名を取得する
	 * @param code
	 * @return
	 */
	private String getClassName(String code) {
		
		//改行と{ } を削除
		code = code.replace("\n", "");
		code = code.replace("\r", "");
		code = code.replace("{", "");
		code = code.replace("}", "");
		
		String[] codeArry = code.split(" ");
		
		boolean findFlg = false;
		String ClassName = "";
		for( String word : codeArry) {
			if( word.equals("class")) {
				findFlg = true;
			}else if( findFlg && word.length() > 0) {
				//classの後の言葉
				ClassName = word;
				break;
			}
		}
		
		if( ClassName.length() == 0) {
			return AZCafeConfig.getInstance().getSrcPrefix();
		}
		
		return ClassName;
	}
}
