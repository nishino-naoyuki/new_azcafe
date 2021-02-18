package jp.ac.asojuku.azcafe.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jp.ac.asojuku.validator.UserValidator;
import lombok.Data;


@Component
@ConfigurationProperties(prefix = "azcafe")
@Data
public class AZCafeConfig {

	private  static AZCafeConfig config;
	
	@Autowired
	public void setCofing(AZCafeConfig config) {
		AZCafeConfig.config = config;
	}
	
	public static AZCafeConfig getInstance() {
		return AZCafeConfig.config;
	}
	
	private String salt;	//パスワードハッシュソルト
	private String avaterbasedir;//
	private Integer tokenlimit;	//自動ログイン期限（日）
	private String passwordpolicy;	//パスワードポリシー
	private String csvoutputdir;	//CSVの出力ディレクトリ
	private String csvoutputencode;	//CSVの出力ファイルのエンコード
	private String csvuploaddir;	//CSVのアップロードディレクトリ
	private String batchdir;	//バッチファイルがあるディレクトリ
	private String codedir;	//ソースコードのベースディレクトリ
	private String srcPrefix;	//ソースファイルのプレフィクス
	private String batchfileext;	//バッチファイルの拡張子
	private String gradingjavac;	//javac実行のシェル（バッチ）名
	private String gradingjava;	//java実行のシェル（バッチ）名
	private String checkstyle;	//checkstyle実行のシェル（バッチ）名
	private String gradingjavascript;	//javascript実行のシェル（バッチ）名
	private String nologindisplay;	//ログイン無しで表示できる画面のリスト（CSV）
	private String studentdenied;	//学生が見れない画面のリスト（CSV）
	private Integer handminus;
	private Map<String, Integer> difficulty;
}
