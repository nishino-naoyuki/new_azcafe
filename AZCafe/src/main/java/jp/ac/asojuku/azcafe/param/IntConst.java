package jp.ac.asojuku.azcafe.param;

public class IntConst {
	public static Integer ALL_BBS_DATA = -1;
	//ダッシュボードに表示する掲示板情報の更新基準期間（10s単位）
	public static Long BBS_UPDATE_TERM = (long)(7*24*60*60*10);	
	//ダッシュボードに表示するチャット情報の更新基準期間（10s単位）
	public static Long CHAT_UPDATE_TERM = (long)(3*24*60*60*10);
	//自動ログインのクッキー寿命（単位：秒）
	public static Integer AUTO_LOGIN_COOKIE = (60*60*24*5);
	//ABCC全体のルームID（＝お知らせの時のルームID）
	public static Integer ALL_ROOM_ID = 1;
	//お知らせ情報のカテゴリ名
	public static String INFO_CATE_NAME = "お知らせ";
	

}
