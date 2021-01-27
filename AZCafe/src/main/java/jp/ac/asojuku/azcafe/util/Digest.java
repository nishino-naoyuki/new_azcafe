/**
 *
 */
package jp.ac.asojuku.azcafe.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;
import jp.ac.asojuku.azcafe.exception.AZCafeException;


/**
 * ハッシュクラス
 * @author nishino
 *
 */
@Component
public class Digest {

	public static String create(String msg){
		// 16進数文字列でMD5値を取得する
		String hexString = DigestUtils.md5Hex(msg);

		return hexString;
	}

	/**
	 * パスワードのハッシュ値を算出する
	 * パスワードのハッシュは
	 * 　ソルト+メアド+パスワード+ソルト
	 * で算出する
	 * @param mailadress
	 * @param pwd
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	public static String createPassword(String mailadress,String pwd) throws AZCafeException{

		//パスワードソルトを取得
		String pwdHashSalt = AZCafeConfig.getInstance().getSalt();
		// 16進数文字列でMD5値を取得する
		String hexString = DigestUtils.md5Hex(pwdHashSalt+mailadress+pwd+pwdHashSalt);

		return hexString;
	}

	/**
	 * 暗号化メソッド
	 *
	 * @param text 暗号化する文字列
	 * @return 暗号化文字列
	 */
	public static String encrypt(String text,String keys,String ivs) {
		// 変数初期化
		String strResult = null;

		try {
			// 文字列をバイト配列へ変換
			byte[] byteText = text.getBytes("UTF-8");

			// 暗号化キーと初期化ベクトルをバイト配列へ変換
			byte[] byteKey = keys.getBytes("UTF-8");
			byte[] byteIv = ivs.getBytes("UTF-8");

			// 暗号化キーと初期化ベクトルのオブジェクト生成
			SecretKeySpec key = new SecretKeySpec(byteKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(byteIv);

			// Cipherオブジェクト生成
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// Cipherオブジェクトの初期化
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);

			// 暗号化の結果格納
			byte[] byteResult = cipher.doFinal(byteText);

			// Base64へエンコード
			strResult = Base64.encodeBase64String(byteResult);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		// 暗号化文字列を返却
		return strResult;
	}

	/**
	 * 復号化メソッド
	 *
	 * @param text 復号化する文字列
	 * @return 復号化文字列
	 */
	public static String decrypt(String text,String keys,String ivs) {
		// 変数初期化
		String strResult = null;

		try {
			// Base64をデコード
			byte[] byteText = Base64.decodeBase64(text);

			// 暗号化キーと初期化ベクトルをバイト配列へ変換
			byte[] byteKey = keys.getBytes("UTF-8");
			byte[] byteIv = ivs.getBytes("UTF-8");

			// 復号化キーと初期化ベクトルのオブジェクト生成
			SecretKeySpec key = new SecretKeySpec(byteKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(byteIv);

			// Cipherオブジェクト生成
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			// Cipherオブジェクトの初期化
			cipher.init(Cipher.DECRYPT_MODE, key, iv);

			// 復号化の結果格納
			byte[] byteResult = cipher.doFinal(byteText);

			// バイト配列を文字列へ変換
			strResult = new String(byteResult, "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		// 復号化文字列を返却
		return strResult;
	}
}
