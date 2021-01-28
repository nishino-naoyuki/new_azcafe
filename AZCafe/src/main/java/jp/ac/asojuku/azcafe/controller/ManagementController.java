package jp.ac.asojuku.azcafe.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.csv.UserCSV;
import jp.ac.asojuku.azcafe.dto.CSVProgressDto;
import jp.ac.asojuku.azcafe.dto.CreateUserDto;
import jp.ac.asojuku.azcafe.dto.HomeroomDto;
import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.err.ErrorCode;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.form.UserInputCSVForm;
import jp.ac.asojuku.azcafe.form.UserInputForm;
import jp.ac.asojuku.azcafe.param.RoleId;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.HomeroomService;
import jp.ac.asojuku.azcafe.service.UserCSVService;
import jp.ac.asojuku.azcafe.service.UserService;
import jp.ac.asojuku.azcafe.util.FileUtils;
import jp.ac.asojuku.validator.UserValidator;

/**
 * 管理コントローラ
 * @author nishino
 *
 */
@Controller
@RequestMapping(value= {"/manage"})
public class ManagementController {
	private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserCSVService userCSVService;
	
	@Autowired
	HomeroomService homeroomService;

	/**
	 * 登録の入力画面を表示する
	 * 
	 * @param mv
	 * @return
	 */
	@RequestMapping(value= {"/index"}, method=RequestMethod.GET)
	public ModelAndView manage(ModelAndView mv) {
		
		//クラス一覧取得
		List<HomeroomDto> list = homeroomService.getAll();
				
		mv.addObject("homeroomList", list);
        mv.addObject("userInputForm",new UserInputForm());
		mv.addObject("completeflg", false);
		mv.addObject("kind", "user");
        
		 mv.setViewName("management");
		 return mv;
	}
	
	/**
	 * 登録画面の入力チェックをする
	 * 
	 * @param mv
	 * @param userInputForm
	 * @param bindingResult
	 * @return
	 * @throws AZCafeException
	 */
	@RequestMapping(value= {"/user_confirm"}, method=RequestMethod.POST)
    public ModelAndView user_confirm(ModelAndView mv,
    		@Valid UserInputForm userInputForm,
			BindingResult bindingResult) throws AZCafeException  {
		
		//クラス一覧取得
		List<HomeroomDto> list = homeroomService.getAll();
		//入力チェックを行う
		validateRequestParams(userInputForm,list,bindingResult);
		//エラーがある場合は入力画面へ戻る
		if( bindingResult.hasErrors() ) {
			//エラー情報をセットする
			mv.addObject("homeroomList", list);
			mv.addObject("completeflg", false);
			mv.addObject("kind", "user");
	        mv.setViewName("management");
		}else {
			//アイコンファイルをアップロード
			String iconPath = FileUtils.uploadIconFile( userInputForm.getIcon() );
			//登録情報を保存する
			session.setAttribute(SessionConst.USERDTO,getCreateUserDtoFrom(userInputForm,iconPath));
	        mv.setViewName("redirect:user_register");
		}
		return mv;
	}
	
	@RequestMapping(value= {"/user_register"}, method=RequestMethod.GET)
	public ModelAndView user_register(ModelAndView mv) throws AZCafeException {
		
		CreateUserDto createUserDto = (CreateUserDto)session.getAttribute(SessionConst.USERDTO);
		
		//登録
		userService.insert(createUserDto);
		
		//セッションから情報削除
		session.removeAttribute(SessionConst.USERDTO);

		List<HomeroomDto> list = homeroomService.getAll();
				
		mv.addObject("homeroomList", list);
        mv.addObject("userInputForm",new UserInputForm());
		mv.addObject("completeflg", true);
		mv.addObject("kind", "user");
		
		mv.setViewName("management");
		return mv;
	}
	
	public Object csvinput(@Valid UserInputCSVForm userInputCSVForm,BindingResult err) throws AZCafeException {
		 //ディレクトリを作成する
	    File uploadDir = mkCSVUploaddirs();

	    //出力ファイル名を決定する
	    File uploadFile = new File(uploadDir.getPath() + "/" + "test.csv");
	    //アップロードファイルを取得
	    byte[] bytes = userInputCSVForm.getUploadFile().getBytes();
	    //出力ストリームを取得
	    BufferedOutputStream uploadFileStream =
                new BufferedOutputStream(new FileOutputStream(uploadFile));
	    //ストリームに書き込んでクローズ
	    uploadFileStream.write(bytes);
        uploadFileStream.close();
        
        //エラーチェックを行う
        List<UserCSV> userList = userCSVService.checkForCSV(uploadFile.getAbsolutePath(),err,"");

		//if( errors.isHasErr() ){
        if(err.hasErrors()) {
			String jsonMsg =  outputErrorResult(err);
			return jsonMsg;
		}
        
	}
	

    /**
     * CSVファイルアップロード用のディレクトリを作成する
     * @return
     * @throws AZCafeException
     */
    private File mkCSVUploaddirs() throws AZCafeException{
    	
    	//アップロードディレクトリを取得する
    	StringBuffer filePath = new StringBuffer(AZCafeConfig.getInstance().getCsvuploaddir());
    	
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
	 * FORMからDTOを作成する
	 * @param userInputForm
	 * @return
	 */
	private CreateUserDto getCreateUserDtoFrom(UserInputForm userInputForm,String iconPath) {
		CreateUserDto createUserDto = new CreateUserDto();
		
		//ICON
		createUserDto.setIconFileName(iconPath);
		createUserDto.setRoleId(userInputForm.getRoleId());
		createUserDto.setStudentNo(userInputForm.getStudentNo());
		createUserDto.setHomeroomId(userInputForm.getHomeroomId());
		createUserDto.setMail(userInputForm.getMail());
		createUserDto.setNickname(userInputForm.getNickname());
		createUserDto.setPass(userInputForm.getPass1());
		createUserDto.setAdmissionYear(Integer.valueOf(userInputForm.getAdmissionYear()));
		
		
		return createUserDto;
	}

	/**
	 * ユーザー登録の情報をチェックする
	 * 
	 * @param userInputForm
	 * @param err
	 * @throws AZCafeException
	 */
	private void validateRequestParams(
			UserInputForm userInputForm,
			List<HomeroomDto> list,
			BindingResult err) throws AZCafeException {
		
		//学籍番号（学生のみチェック）
		if( RoleId.STUDENT.equals( userInputForm.getRoleId() ) && 
				userService.isExistStudentNo(userInputForm.getStudentNo()) ) {
			UserValidator.setErrorcode("studentNo",err,ErrorCode.ERR_MEMBER_ENTRY_DUPLICATE_STUDENTNO);
		}
		
		//ニックネーム（Formでチェック済み）
		//UserValidator.useNickName(userInputForm.getNickname(),err);
		
		//メールアドレス
		UserValidator.mailAddress(userInputForm.getMail(),err);
		if( userService.isExistMailadress(userInputForm.getMail()) ) {
			UserValidator.setErrorcode("mail",err,ErrorCode.ERR_MEMBER_ENTRY_DUPLICATE_MEIL);
		}
		
		//パスワード
		if( userInputForm.getPass1().equals(userInputForm.getPass2()) != true) {
			//パスワード不一致
			UserValidator.setErrorcode("pass1",err,ErrorCode.ERR_MEMBER_ENTRY_PASSWORD_NOTMATCH);
		}
		UserValidator.password(userInputForm.getPass1(),err);
		
		//ロールID
		UserValidator.roleId(userInputForm.getRoleId().toString(),err);
		
		//入学年度
		UserValidator.admissionYear(userInputForm.getAdmissionYear(),err);
		
		return ;
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
