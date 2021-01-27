package jp.ac.asojuku.azcafe.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;

public class Token {
	private static int TOKEN_LENGTH = 16;  //16*2=32バイト


	//32バイトのCSRFトークンを作成
	public static String getCsrfToken() {
		byte token[] = new byte[TOKEN_LENGTH];
		StringBuffer buf = new StringBuffer();
		SecureRandom random = null;
	
		try {
				random = SecureRandom.getInstance("SHA1PRNG");
				random.nextBytes(token);
	
				for (int i = 0; i < token.length; i++) {
					buf.append(String.format("%02x", token[i]));
				}
	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return buf.toString();
	}
}
