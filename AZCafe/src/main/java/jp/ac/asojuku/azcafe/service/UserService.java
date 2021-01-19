package jp.ac.asojuku.azcafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.azcafe.dto.LoginInfoDto;
import jp.ac.asojuku.azcafe.entity.UserTblEntity;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.RoleId;
import jp.ac.asojuku.azcafe.repository.UserRepository;
import jp.ac.asojuku.azcafe.util.Digest;

@Service
public class UserService {

	@Autowired 
	UserRepository userRepository;
	
	/**
	 * ログイン処理を行う
	 * @param mail
	 * @param password
	 * @return ログイン情報（ログイン失敗時はnull）
	 * @throws AZCafeException
	 */
	public LoginInfoDto login(String mail,String password) 
			throws AZCafeException{
		LoginInfoDto dto = null;
		
		//ハッシュ計算する
		String hashedPwd  = Digest.createPassword(mail, password);

		//ログインユーザー検索
		UserTblEntity entity = userRepository.getUser(mail, hashedPwd);
		
		//Entity→DTO
		if( entity!= null ) {
			dto = new LoginInfoDto();
			
			dto.setUserId(entity.getUserId());
			dto.setMail(entity.getMail());
			dto.setGrade(entity.getGrade());
			dto.setNickName(entity.getNickName());
			dto.setRole( RoleId.search(entity.getRole()) );
			dto.setRoleName(RoleId.search(entity.getRole()).getMsg());
			dto.setCourseName( entity.getHomeroomTbl().getName() );
			dto.setGrade(entity.getGrade());
			dto.setLevelName(entity.getLevelTbl().getName());
			dto.setAvater(entity.getAvater());
		}
		
		return dto;
	}
}
