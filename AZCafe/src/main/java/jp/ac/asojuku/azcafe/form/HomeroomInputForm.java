package jp.ac.asojuku.azcafe.form;

import javax.validation.constraints.NotEmpty;


import lombok.Data;

/**
 * ホームルーム名更新のフォーム
 * @author nishino
 *
 */
@Data
public class HomeroomInputForm {

	private Integer id;
	
	@NotEmpty(message = "{errmsg0105}")
	private String name;
}
