package jp.ac.asojuku.azcafe.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.dto.GradingResultDto;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.form.GradingByFileForm;
import jp.ac.asojuku.azcafe.form.GradingByTextForm;
import jp.ac.asojuku.azcafe.param.Language;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.GradingService;
import jp.ac.asojuku.azcafe.util.FileUtils;

/**
 * 採点コントローラ
 * @author nishino
 *
 */
/**
 * @author nishino
 *
 */
@RestController
@RequestMapping(value= {"/grading"})
public class GradingController {
	private static final Logger logger = LoggerFactory.getLogger(GradingController.class);
	
	@Autowired
	GradingService gradingService;
	
	@Autowired
	HttpSession session;

	@RequestMapping(value= {"/text"}, method=RequestMethod.POST)
	public Object gradingByText(
			@Valid GradingByTextForm form) throws AZCafeException, JsonProcessingException {
		
		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		
		//採点実行
		GradingResultDto result = gradingService.execByText(
																loginInfo.getUserId(), 
																form.getAssignmentId(),
																Language.getBy(form.getLanguage()), 
																form.getAnswerText());

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(result);
		
        logger.trace("jsonString:{}",jsonString);
        
		return jsonString;
	}

	/**
	 * ファイルによる採点
	 * 
	 * @param form
	 * @return
	 * @throws AZCafeException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@RequestMapping(value= {"/file"}, method=RequestMethod.POST)
	public Object gradingByFile(
			@Valid GradingByFileForm form) throws AZCafeException, FileNotFoundException, IOException {

		//セッションからログイン情報を取得
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		if( loginInfo == null ) {
			//ログイン情報が無いことはないはずだが、直リンされた場合を一応考慮
			throw new AZCafeException("ログイン情報がありません。ログインしなおしてください");
		}
		
		List<File> fileList = getUploadFileList(loginInfo.getUserId(),form);

		//採点実行
		GradingResultDto result = gradingService.execByFile(
																loginInfo.getUserId(), 
																form.getAssignmentId(),
																Language.JAVA,
																fileList);

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(result);
		
        logger.trace("jsonString:{}",jsonString);
        
		return jsonString;
	}
	/**
	 * アップロードファイルを取得する
	 * @param userId
	 * @param form
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private List<File> getUploadFileList(Integer userId,GradingByFileForm form) throws FileNotFoundException, IOException{
		MultipartFile[] files = {
				form.getAnswerFile1(),
				form.getAnswerFile2(),
				form.getAnswerFile3(),
				form.getAnswerFile4(),
				form.getAnswerFile5(),
		};
		
		List<File> fileList = new ArrayList<>();

    	String uploadDir = getUploadDir(userId);
		
		for( MultipartFile uploadFormFile : files ) {
			if( uploadFormFile != null && !uploadFormFile.isEmpty()) {
				//ファイルを作成する
			    File uploadFile = new File(uploadDir,uploadFormFile.getOriginalFilename());
	
			    //出力ストリームを取得
			    try(BufferedOutputStream uploadFileStream =
		                new BufferedOutputStream(new FileOutputStream(uploadFile))){
				    //出力ファイル名を決定する
					byte[] bytes = uploadFormFile.getBytes();
				    //ストリームに書き込んでクローズ
				    uploadFileStream.write(bytes);
			    }
			    fileList.add(uploadFile);
			}
		}
		
		return fileList;
	}
	
	/**
	 * アップロードディレクトリを作成し取得する
	 * @param userId
	 * @return
	 */
	private String getUploadDir(Integer userId) {

    	StringBuilder sb = new StringBuilder(AZCafeConfig.getInstance().getCsvuploaddir());
    	sb.append("/").append(userId);
        // フォルダ作成
        FileUtils.makeDir( sb.toString());
        
        return sb.toString();
	}
}
