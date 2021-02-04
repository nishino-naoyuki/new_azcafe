package jp.ac.asojuku.azcafe.param;

/**
 * 言語タイプ
 * @author nishino
 *
 */
public enum Language {

	JAVA(0,"java","java"),
	JAVASCRIPT(1,"javascript","js");

	//ステータス
	private int id;
	private String msg;
	private String ext;	//拡張子

	private Language(int id, String msg,String ext) {
		this.id = id;
		this.msg = msg;
		this.ext = ext;
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
	
	public String getExt() {
		return ext;
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
	public static Language getBy(Integer id) {
		Language ret = null;
		switch(id) {
		case 0:
			ret = JAVA;
			break;
		case 1 :
			ret = JAVASCRIPT;
			break;
		}
		
		return ret;
	}
}
