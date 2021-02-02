package jp.ac.asojuku.azcafe.param;

public enum Difficulty {

	VEASY(1,"簡単"),
	EASY(2,"やや簡単"),
	NORMAL(3,"普通"),
	DIFF(4,"やや難"),
	VDIFF(5,"難");

	//ステータス
	private int id;
	private String msg;

	private Difficulty(int id, String msg) {
		this.id = id;
		this.msg = msg;
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
