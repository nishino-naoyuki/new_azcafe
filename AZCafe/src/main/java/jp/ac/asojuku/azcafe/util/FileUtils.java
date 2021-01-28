package jp.ac.asojuku.azcafe.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.exception.AZCafeException;

public class FileUtils {
	Logger logger = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * アイコンファイルのアップロード
	 * アップロードディレクトリは　ベースDIR/ユーザーID/ファイル名
	 * @param userId
	 * @param multipartFile
	 * @return
	 * @throws AZCafeException
	 */
	public static String uploadIconFile(MultipartFile multipartFile) throws AZCafeException  {
		
		if( multipartFile == null ) {
			return null;
		}
		
		String imgdir = AZCafeConfig.getInstance().getAvaterbasedir() ;
		
		//ディレクトリ（なければ作成）
		makeDir(imgdir);
		
		//ファイル名を作成する
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateFormat = sdf.format(now);
		
		String fileName = "icon_" + dateFormat + multipartFile.getOriginalFilename();
		
		//ファイルをコピーする
		File uploadFilePath = new File(imgdir + "/" + fileName);
		try {
			multipartFile.transferTo(uploadFilePath);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new AZCafeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new AZCafeException(e);
		}
		
		return fileName;
	}

	/**
	 * ディレクトリ作成
	 * @param dir
	 */
	public static void makeDir(String dir){
		//Fileオブジェクトを生成する
        File f = new File(dir);

        if (!f.exists()) {
            //フォルダ作成実行
            f.mkdirs();
        }
	}
}
