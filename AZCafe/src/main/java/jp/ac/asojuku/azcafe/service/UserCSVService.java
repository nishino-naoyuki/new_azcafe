package jp.ac.asojuku.azcafe.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.opencsv.bean.CsvToBeanBuilder;

import jp.ac.asojuku.azcafe.csv.UserCSV;
import jp.ac.asojuku.azcafe.entity.HomeroomTblEntity;
import jp.ac.asojuku.azcafe.entity.LevelTblEntity;
import jp.ac.asojuku.azcafe.entity.UserTblEntity;
import jp.ac.asojuku.azcafe.err.ErrorCode;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.RoleId;
import jp.ac.asojuku.azcafe.repository.UserRepository;
import jp.ac.asojuku.azcafe.util.Digest;
import jp.ac.asojuku.validator.UserValidator;

@Service
public class UserCSVService {
	private static final Logger logger = LoggerFactory.getLogger(UserCSVService.class);
	@Autowired 
	UserRepository userRepository;
	

	/**
	 * CSVファイルの中身をチェックする
	 * 
	 * @param csvPath
	 * @param errors
	 * @param type
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	public List<UserCSV> checkForCSV(String csvPath, BindingResult err,String type) throws AZCafeException {
		List<UserCSV> list = null;
		FileReader fileReader = null;
		
		try {
			///////////////////////////////
			//CSVを読み込みマッピング
			fileReader = new FileReader(csvPath); 
			list = new CsvToBeanBuilder<UserCSV>(
                    fileReader).withType(UserCSV.class).build().parse(); 

            // エラーチェック
            for(UserCSV userCsv : list){
        		UserValidator.useName(userCsv.getName(), err);
        		UserValidator.useNickName(userCsv.getNickName(), err);
        		UserValidator.roleId(String.valueOf(userCsv.getRoleId()), err);
        		if( RoleId.STUDENT.equals(userCsv.getRoleId())){
        			UserValidator.admissionYear(userCsv.getAdmissionYear(), err);
        		}
        		UserValidator.mailAddress(userCsv.getMailAddress(), err);
        		UserValidator.password(userCsv.getPassword(), err);
            }
            
//            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvPath), "SJIS"), ',', '"', 1);
//            ColumnPositionMappingStrategy<UserCSV> strat = new ColumnPositionMappingStrategy<UserCSV>();
//            strat.setType(UserCSV.class);
//            strat.setColumnMapping(HEADER);
//            CsvToBean<UserCSV> csv = new CsvToBean<UserCSV>();
//            list = csv.parse(strat, reader);
		}catch (Exception e) {
        	logger.warn("CSVパースエラー：",e);
        	UserValidator.setErrorcode("",err,ErrorCode.ERR_CSV_FORMAT_ERROR);
        }finally {
        	if( fileReader != null ) {
        		try {
					fileReader.close();
				} catch (IOException e) {
					;//ignore
				}
        	}
        }
		
		return list;
	}
	
	/**
	 * CSV用の追加・更新処理
	 * 
	 * @param userList
	 * @throws AsoBbsSystemErrException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertByCSV(List<UserCSV> userList) throws AZCafeException {
		
		for(UserCSV csv : userList) {
			insertOrUpdate(csv);
		}
		
	}
	
	/**
	 * CSV登録・更新処理
	 * 
	 * @param userCSV
	 * @throws AsoBbsSystemErrException 
	 */
	private void insertOrUpdate(UserCSV userCSV) throws AZCafeException {
		//すでに登録済みかどうかをチェック
		UserTblEntity entity = userRepository.getUserByMail(userCSV.getMailAddress());
		
		UserTblEntity iuEntity = createEntityFromUserCSV(userCSV,entity);
		if( entity == null ) {
			//追加
			userRepository.save(iuEntity);
		}else {
			//更新
			iuEntity.setUserId(entity.getUserId());
			userRepository.save(iuEntity);
		}
	}
	
	/**
	 * UserCSVからUserEntityを作成する
	 * 
	 * @param userCSV
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	private UserTblEntity createEntityFromUserCSV(UserCSV userCSV,UserTblEntity entity) throws AZCafeException {
		
		if( entity == null ) {
			entity = new UserTblEntity();
			LevelTblEntity levelTblEntity = new LevelTblEntity();
			levelTblEntity.setLevelId(1);
			entity.setLevelTbl(levelTblEntity);
			entity.setFollowerNum(0);
			entity.setFollowNum(0);
			entity.setGoodNum(0);
			entity.setGrade(1);
			entity.setPoint(0);
		}
		
		//ハッシュ計算する
		String hashedPwd  = Digest.createPassword(userCSV.getMailAddress(), userCSV.getPassword());
		
		entity.setGrade(1);//後でバッチで更新
		entity.setMail(userCSV.getMailAddress());
		entity.setName(userCSV.getName());
		entity.setNickName(userCSV.getNickName());
		entity.setPassword(hashedPwd);
		entity.setOrgNo(userCSV.getName());
		entity.setEnterYear(Integer.parseInt(userCSV.getAdmissionYear()));
		entity.setIntroduction("");
		
		HomeroomTblEntity homeroomTblEntity = new HomeroomTblEntity();
		homeroomTblEntity.setHomeroomId(userCSV.getCourseId());
		entity.setHomeroomTbl(homeroomTblEntity);
		
		entity.setRole(userCSV.getRoleId());
		
		
		return entity;
	}
}
