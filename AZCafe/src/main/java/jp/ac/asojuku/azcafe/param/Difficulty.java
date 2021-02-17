package jp.ac.asojuku.azcafe.param;

import java.util.Map;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;

public enum Difficulty {

	VEASY(1,"簡単","veasy"),
	EASY(2,"やや簡単","easy"),
	NORMAL(3,"普通","normal"),
	DIFF(4,"やや難","diff"),
	VDIFF(5,"難","vdiff");

	//ステータス
	private int id;
	private String msg;
	private String key;

	private Difficulty(int id, String msg,String key) {
		this.id = id;
		this.msg = msg;
		this.key = key;
	}
	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return msg1
	 */
	public String getMsg() {
		return msg;
	}

	public boolean equals(Integer id){
		if(id == null){
			return false;
		}

		return (this.id == id);
	}
	
	public int getPoint() {
		Map<String, Integer> pointMap = AZCafeConfig.getInstance().getDifficulty();
		
		return pointMap.get(key);
	}
	/**
	 * IDからDifficultyオブジェクトを取得する
	 * @param id
	 * @return
	 */
	public static Difficulty getBy(Integer id) {
		Difficulty ret = null;
		switch(id) {
		case 1:
			ret = VEASY;
			break;
		case 2 :
			ret = EASY;
			break;
		case 3 :
			ret = NORMAL;
			break;
		case 4 :
			ret = DIFF;
			break;
		case 5 :
			ret = VDIFF;
			break;
		}
		
		return ret;
	}
}
