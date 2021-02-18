package jp.ac.asojuku.azcafe.csv;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class UserRankingCSV {
	//学科
	@CsvBindByName(column = "0.順位") 
	private int rank;
	//学科
	@CsvBindByName(column = "1.学科") 
	private String hoomroomName;
	//学籍番号/社員番号
	@CsvBindByName(column = "2.学籍番号") 
	private String orgNo;
	//名前
	@CsvBindByName(column = "3.名前") 
	private String name;
	//ニックネーム
	@CsvBindByName(column = "4.ニックネーム") 
	private String nickName;
	//ポイント
	@CsvBindByName(column = "5.ポイント") 
	private int point;
}
