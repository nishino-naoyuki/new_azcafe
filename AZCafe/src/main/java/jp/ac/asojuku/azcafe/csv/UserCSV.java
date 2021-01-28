package jp.ac.asojuku.azcafe.csv;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class UserCSV {
	//ロールID
	@CsvBindByName(column = "ロールID") 
	private int roleId;
	//学籍番号/社員番号
	@CsvBindByName(column = "学籍番号/社員番号") 
	private String name;
	//メールアドレス
	@CsvBindByName(column = "メールアドレス") 
	private String mailAddress;
	//ニックネーム
	@CsvBindByName(column = "ニックネーム") 
	private String nickName;
	//学科
	@CsvBindByName(column = "学科") 
	private int courseId;
	//パスワード
	@CsvBindByName(column = "パスワード") 
	private String password;
	//入学年度
	@CsvBindByName(column = "入学年度") 
	private String admissionYear;
}
