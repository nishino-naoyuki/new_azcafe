package jp.ac.asojuku.azcafe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


@Component
@ConfigurationProperties(prefix = "azcafe")
@Data
public class AZCafeConfig {
	
	private String salt;	//パスワードハッシュソルト
	private String avaterbasedir;//
	private Integer tokenlimit;	//自動ログイン期限（日）
}
