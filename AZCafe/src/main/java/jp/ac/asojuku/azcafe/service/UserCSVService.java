package jp.ac.asojuku.azcafe.service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.csv.UserAnswerCSV;
import jp.ac.asojuku.azcafe.csv.UserCSV;
import jp.ac.asojuku.azcafe.csv.UserRankingCSV;
import jp.ac.asojuku.azcafe.dto.RankingDto;
import jp.ac.asojuku.azcafe.dto.UserSearchElementDto;
import jp.ac.asojuku.azcafe.entity.AnswerTblEntity;
import jp.ac.asojuku.azcafe.entity.AssignmentTblEntity;
import jp.ac.asojuku.azcafe.entity.HomeroomTblEntity;
import jp.ac.asojuku.azcafe.entity.LevelTblEntity;
import jp.ac.asojuku.azcafe.entity.UserTblEntity;
import jp.ac.asojuku.azcafe.err.ErrorCode;
import jp.ac.asojuku.azcafe.exception.AZCafeException;
import jp.ac.asojuku.azcafe.param.RoleId;
import jp.ac.asojuku.azcafe.repository.AnswerRepository;
import jp.ac.asojuku.azcafe.repository.AssignmentRepository;
import jp.ac.asojuku.azcafe.repository.UserRepository;
import jp.ac.asojuku.azcafe.util.Digest;
import jp.ac.asojuku.azcafe.util.FileUtils;
import jp.ac.asojuku.validator.UserValidator;

@Service
public class UserCSVService {
	private static final Logger logger = LoggerFactory.getLogger(UserCSVService.class);
	@Autowired 
	UserRepository userRepository;
	@Autowired
	AssignmentRepository assignmentRepository;
	@Autowired
	AnswerRepository answerRepository;
	
	/**
	 * @param userList
	 * @param csvPath
	 * @return
	 */
	public boolean outputUserInfo(List<UserSearchElementDto> userList,String csvPath) {
		boolean result = true;
		
		//////////////////////////////////////////////////
		//問題の一覧を取得する
		List<AssignmentTblEntity> assignList = assignmentRepository.findAll(Sort.by("assignmentId"));
		//////////////////////////////////////////////////
		//CSVのヘッダーをセット
		UserAnswerCSV header = getUserAnswerCSVHeader(assignList);
		//////////////////////////////////////////////////
		//CSVのデータ部をセット
		List<UserAnswerCSV> userAnswerList = getUserAnswerCSVList(userList,assignList);
		//////////////////////////////////////////////////
		//ファイル出力
		result = outputUserAnswerCSV(csvPath,header,userAnswerList);
		
		return result;
	}
	
	/**
	 * CSVファイルを出力する
	 * 
	 * @param csvPath
	 * @param header
	 * @param userAnswerList
	 * @return
	 */
	private boolean outputUserAnswerCSV(
			String csvPath,
			UserAnswerCSV header,
			List<UserAnswerCSV> userAnswerList) {
		
		boolean result = true;

		List<String> csvInfo = new ArrayList<>();
		
		csvInfo.add(header.toString());
		for(UserAnswerCSV csvelement : userAnswerList) {
			csvInfo.add(csvelement.toString());
		}

		//ファイル出力
		String encode = AZCafeConfig.getInstance().getCsvoutputencode();
		try {
			FileUtils.outputFile(csvPath, csvInfo, encode);
		} catch (AZCafeException e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	/**
	 * CSVデータ部を出力する
	 * @param userList
	 * @param assignList
	 * @return
	 */
	private List<UserAnswerCSV> getUserAnswerCSVList(
			List<UserSearchElementDto> userList,List<AssignmentTblEntity> assignList){
		List<UserAnswerCSV> userAnswerList = new ArrayList<>();

		for( UserSearchElementDto userDto : userList) {
			UserAnswerCSV uaElement = new UserAnswerCSV();
			
			//ユーザーの基本情報をセットする
			uaElement.addData(userDto.getHomeroomeName());	//学科名
			uaElement.addData(userDto.getOrgNo());	//学籍番号
			uaElement.addData(userDto.getNickName());	//ニックネーム
			uaElement.addData(userDto.getName());	//名前
			//ユーザーごとの解答リストを取得する
			List<AnswerTblEntity> ansList = answerRepository.getList(userDto.getUserId());
			for(AssignmentTblEntity assign : assignList ) {
				AnswerTblEntity ans = containAnswer(ansList,assign.getAssignmentId());
				if( ans != null ) {
					//解答済み
					uaElement.addData("〇");
				}else {
					uaElement.addData("---");
				}
			}			
			userAnswerList.add(uaElement);
		}
		
		return userAnswerList;
	}
	
	/**
	 * CSVヘッダ部を出力する
	 * @param assignList
	 * @return
	 */
	private UserAnswerCSV getUserAnswerCSVHeader(List<AssignmentTblEntity> assignList) {
		UserAnswerCSV header = new UserAnswerCSV();
		
		//学科、学籍番号、ニックネーム、名前のあとに問題のタイトルが並ぶ
		header.addData("学科名");
		header.addData("学籍");
		header.addData("ニックネーム");
		header.addData("名前");
		for( AssignmentTblEntity entity : assignList) {
			header.addData(entity.getTitle());
		}
		
		return header;
	}
	
	private AnswerTblEntity containAnswer(List<AnswerTblEntity> ansList , Integer assignmentId) {
		AnswerTblEntity ansEntity = null;
		
		for(AnswerTblEntity ans : ansList ) {
			if( ans.getAssignmentId() == assignmentId && ans.getCorrectFlg() == 1) {
				ansEntity = ans;
				break;
			}
		}
		
		return ansEntity;
	}
	
	
	/**
	 * ランキングCSV出力する
	 * 
	 * @param rankingList　出力するデータ
	 * @param csvPath		出力するファイルのパス
	 * @return
	 */
	public boolean outputRnakingCsv(List<RankingDto> rankingList,String csvPath) {
		boolean result = true;
		
		List<UserRankingCSV> rankCsvList = new ArrayList<>();
		//CSV情報を作成する
		for(RankingDto dto : rankingList ) {
			UserRankingCSV csvElement = new UserRankingCSV();
			
			csvElement.setRank(dto.getRank());
			csvElement.setOrgNo(dto.getOrgNo());
			csvElement.setName(dto.getName());
			csvElement.setHoomroomName(dto.getHomeroomeName());
			csvElement.setPoint(dto.getPoint());
			csvElement.setNickName(dto.getNickName());
			
			rankCsvList.add(csvElement);
		}
		String encode = AZCafeConfig.getInstance().getCsvoutputencode();
		//出力する
		try(Writer writer = new FileWriter(encode)){
			StatefulBeanToCsv<UserRankingCSV> beanToCsv = new StatefulBeanToCsvBuilder<UserRankingCSV>(writer).build();			
			beanToCsv.write(rankCsvList);
		} catch (CsvDataTypeMismatchException e) {
			e.printStackTrace();
			result = false;
		} catch (CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
			result = false;
		} catch (IOException e1) {
			e1.printStackTrace();
			result = false;
		}
		
		return result;
	}

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
		
		try (FileReader fileReader = new FileReader(csvPath)){
			///////////////////////////////
			//CSVを読み込みマッピング
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
		entity.setOrgNo(userCSV.getOrgNo());
		entity.setEnterYear(Integer.parseInt(userCSV.getAdmissionYear()));
		entity.setIntroduction("");
		
		HomeroomTblEntity homeroomTblEntity = new HomeroomTblEntity();
		homeroomTblEntity.setHomeroomId(userCSV.getCourseId());
		entity.setHomeroomTbl(homeroomTblEntity);
		
		entity.setRole(userCSV.getRoleId());
		
		
		return entity;
	}
}
