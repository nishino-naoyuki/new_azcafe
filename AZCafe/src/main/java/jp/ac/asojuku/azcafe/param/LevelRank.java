package jp.ac.asojuku.azcafe.param;

public enum LevelRank {

	BRONZE(0,"badge-bronze"),
	SILVER(1,"badge-sliver"),
	GOLD(2,"badge-cold"),
	PLATINUM(3,"badge-platinum");

	//ステータス
	private int id;
	private String msg;

	private LevelRank(int id, String msg) {
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
	public static LevelRank getBy(Integer id) {
		LevelRank ret = null;
		switch(id) {
		case 0:
			ret = BRONZE;
			break;
		case 1 :
			ret = SILVER;
			break;
		case 2 :
			ret = GOLD;
			break;
		case 3 :
			ret = PLATINUM;
			break;
		}
		
		return ret;
	}
}
