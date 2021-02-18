package jp.ac.asojuku.azcafe.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.azcafe.dto.CreateUserDto;
import jp.ac.asojuku.azcafe.dto.HomeroomDto;
import jp.ac.asojuku.azcafe.err.ErrorCode;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.form.UserInputForm;
import jp.ac.asojuku.azcafe.param.RoleId;
import jp.ac.asojuku.azcafe.param.SessionConst;
import jp.ac.asojuku.azcafe.service.HomeroomService;
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
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserService userService;
	
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
	
	/**
	 * ユーザーの登録
	 * 
	 * @param mv
	 * @return
	 * @throws AZCafeException
	 */
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
		createUserDto.setName(userInputForm.getName());
		
		
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

	
	
}
