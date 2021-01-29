package jp.ac.asojuku.azcafe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.exception.AZCafeException;

public class FileUtils {
	Logger logger = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * ファイルから1行ごとのデータをリストとして読み込む
	 * @param filePath
	 * @return
	 */
	public static List<String> readLine(String filePath) {
		List<String> lineList = new ArrayList<>();

		if( filePath == null || filePath.length() == 0){
			return lineList;
		}
	    FileReader fr = null;
	    BufferedReader br = null;
	    try {
	        fr = new FileReader(filePath);
	        br = new BufferedReader(fr);

	        String line;
	        while ((line = br.readLine()) != null) {
	        	lineList.add(line);
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if(br != null )
	            	br.close();
	            if(fr != null)
	            	fr.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    return lineList;
	}
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

	/**
	 * ファイル名から拡張子を取り除いた名前を返します。
	 * @param fileName ファイル名
	 * @return ファイル名
	 */
	public static String getPreffix(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(0, point);
	    }
	    return fileName;
	}

	/**
	 * ファイルを比較する
	 * @param fileA
	 * @param fileB
	 * @return
	 */
	public static boolean fileCompare(String fileA, String fileB) {
	    boolean bRet = false;

        List<String> listA = readLine(fileA);
        List<String> listB = readLine(fileB);
        String[] arrayA = listA.toArray(new String[0]);
        String[] arrayB = listB.toArray(new String[0]);

        bRet = Arrays.equals(arrayA, arrayB);

	    return bRet;
	}

	/**
	 * 削除
	 * @param path
	 */
	public static void delete(String path){
		File file = new File(path);

		delete(file);
	}
	public static void delete(File f){

		/*
         * ファイルまたはディレクトリが存在しない場合は何もしない
         */
        if(f.exists() == false) {
            return;
        }

        if(f.isFile()) {
            /*
             * ファイルの場合は削除する
             */
            f.delete();

        } else if(f.isDirectory()){
            /*
             * ディレクトリの場合は、すべてのファイルを削除する
             */

            /*
             * 対象ディレクトリ内のファイルおよびディレクトリの一覧を取得
             */
            File[] files = f.listFiles();

            if( files == null )
            	return;
            /*
             * ファイルおよびディレクトリをすべて削除
             */
            for(int i=0; i<files.length; i++) {
                /*
                 * 自身をコールし、再帰的に削除する
                 */
                delete( files[i] );
            }
            /*
             * 自ディレクトリを削除する
             */
            f.delete();
        }

	}

	/**
	 * コピーする
	 *
	 * @param srcPath
	 * @param dstPath
	 * @throws IOException
	 */
	public static void copy(String srcPath,String dstPath) throws IOException{

		File f = new File(dstPath);

		f.getParentFile().mkdirs();

		Files.copy(
				new File(srcPath).toPath(),
				new File(dstPath).toPath());
	}

	/**
	 * パスからファイル名を取得する
	 * @param path
	 * @return
	 */
	public static String getFileNameFromPath(String path){

		File f = new File(path);

		return f.getName();
	}

	/**
	 * 指定されたディレクトリから、指定された拡張子のファイルの一覧を取得する
	 *
	 * @param dir
	 * @param extend
	 * @return
	 */
	public static File[] getFiles(String dir,String extend){

		FilenameFilter filter = null;

		//拡張子の指定があるか？
		if( extend != null && extend.length()>0){
			//フィルタを作成する
			filter = new FilenameFilter() {
				public boolean accept(File file, String str){

					// 拡張子を指定する
					if (str.endsWith(extend)){
						return true;
					}else{
						return false;
					}
				}
			};
		}

		//ファイルの一覧を取得する
		return  new File(dir).listFiles(filter);
	}

}
