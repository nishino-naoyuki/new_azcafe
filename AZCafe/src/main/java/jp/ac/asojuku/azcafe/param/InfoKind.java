package jp.ac.asojuku.azcafe.param;

/**
 * お知らせの種類
 * @author nishino
 *
 */
public enum InfoKind {

	FOLLOW(1,"badge-success","フォロワ"),	//フォロワーの増加
	GOOD(2,"badge-danger","イイネ"),		//GOODが付いた
	COMMENT(3,"badge-info","コメント"),		//コメントが付いた
	ADD(4,"badge-warning","問題追加");

	//ステータス
	private int id;
	private String panel;
	private String title;

	private InfoKind(int id, String panel, String title) {
		this.id = id;
		this.panel = panel;
		this.title = title;
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
	public String getPanel() {
		return panel;
	}
	/**
	 * @return msg1
	 */
	public String getTitle() {
		return title;
	}

	public boolean equals(Integer id){
		if(id == null){
			return false;
		}

		return (this.id == id);
	}
	
	/**
	 * IDからInfoKindオブジェクトを取得する
	 * @param id
	 * @return
	 */
	public static InfoKind getBy(Integer id) {
		InfoKind ret = null;
		switch(id) {
		case 1:
			ret = FOLLOW;
			break;
		case 2 :
			ret = GOOD;
			break;
		case 3 :
			ret = COMMENT;
			break;
		case 4 :
			ret = ADD;
			break;
		}
		
		return ret;
	}
}
