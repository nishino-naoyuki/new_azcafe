package jp.ac.asojuku.azcafe.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.csv.UserCSV;
import jp.ac.asojuku.azcafe.dto.CSVProgressDto;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.form.UserInputCSVForm;
import jp.ac.asojuku.azcafe.service.UserCSVService;
import jp.ac.asojuku.azcafe.util.FileUtils;


@RestController
public class FileController {
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	UserCSVService userCSVService;
	
	@ResponseBody
	@RequestMapping(value = "/getImage/{id}", method = {RequestMethod.GET })
	public HttpEntity<byte[]> getImage(@PathVariable String id) throws IOException {
		
		// リソースファイルを読み込み
		String imgdir = AZCafeConfig.getInstance().getAvaterbasedir();
		//Resource resource = resourceLoader.getResource("classpath:" + "/static/image/" + id);
		Resource resource = resourceLoader.getResource("file:" + imgdir + "/" + id);
		InputStream image = resource.getInputStream();
		
		// byteへ変換
		byte[] b = IOUtils.toByteArray(image);
		
		// レスポンスデータとして返却
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);		
		headers.setContentLength(b.length);
		return new HttpEntity<byte[]>(b, headers);
	}
	

	/**
	 * ユーザー登録CSVの処理
	 * 
	 * @param userInputCSVForm
	 * @param err
	 * @return
	 * @throws AZCafeException
	 * @throws Exception
	 */
	@RequestMapping(value= {"/user/csvinput"}, method=RequestMethod.POST)
	public Object csvinput(@Valid UserInputCSVForm userInputCSVForm,BindingResult err) throws AZCafeException, Exception {
		 //ディレクトリを作成する
	    File uploadDir = mkCSVUploaddirs();

	    //出力ファイル名を決定する
	    File uploadFile = new File(uploadDir.getPath() + "/" + "test.csv");
	    //アップロードファイルを取得
	    byte[] bytes = userInputCSVForm.getUploadFile().getBytes();
	    //出力ストリームを取得
	    try(BufferedOutputStream uploadFileStream =
                new BufferedOutputStream(new FileOutputStream(uploadFile))){
		    //ストリームに書き込んでクローズ
		    uploadFileStream.write(bytes);
	    }
        
        //エラーチェックを行う
        List<UserCSV> userList = userCSVService.checkForCSV(uploadFile.getAbsolutePath(),err,"");

        //もうファイルはいらないので削除
		FileUtils.delete(uploadFile.getParentFile());
		
		//if( errors.isHasErr() ){
        if(err.hasErrors()) {
			String jsonMsg =  outputErrorResult(err);
			return jsonMsg;
		}

		//登録処理
        userCSVService.insertByCSV(userList);

		//処理件数を返す
        return outputResult(userList);
	}
	

    /**
     * CSVファイルアップロード用のディレクトリを作成する
     * @return
     * @throws AZCafeException
     */
    private File mkCSVUploaddirs() throws AZCafeException{
    	
    	//アップロードディレクトリを取得する
    	StringBuilder filePath = new StringBuilder(AZCafeConfig.getInstance().getCsvuploaddir());
    	
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        File uploadDir = new File(filePath.toString(), sdf.format(now));
        // 既に存在する場合はプレフィックスをつける
        int prefix = 0;
        while(uploadDir.exists()){
            prefix++;
            uploadDir =
                    new File(filePath.toString() + sdf.format(now) + "-" + String.valueOf(prefix));
        }

        // フォルダ作成
        FileUtils.makeDir( uploadDir.toString());

        return uploadDir;
    }
    

	/**
	 * 処理結果のJSON文字列を作成する
	 * 
	 * @param userList
	 * @return
	 * @throws JsonProcessingException
	 */
	private String outputResult(List<UserCSV> userList ) throws JsonProcessingException {

		CSVProgressDto progress = new CSVProgressDto();
		
		progress.setTotal(userList.size());
		progress.setNow(userList.size());

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(progress);

        logger.trace("jsonString:{}",jsonString);

        return jsonString;

	}
	

	/**
	 * エラー処理時のJSON文字列を作成する
	 * 
	 * @return
	 * @throws JsonProcessingException
	 */
	private String outputErrorResult(BindingResult err) throws JsonProcessingException {
		CSVProgressDto progress = new CSVProgressDto();
		StringBuffer sb = new StringBuffer();

//		for( ActionError error : errors.getList() ){
//			sb.append( error.getMessage() );
//			sb.append("\n");
//		}
		for( ObjectError error : err.getAllErrors() ){
			sb.append( error.getDefaultMessage() );
			sb.append("\n");
		}
		progress.setErrorMsg(sb.toString());

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(progress);

        logger.trace("jsonString:{}",jsonString);

        return jsonString;
	}
}
