package jp.ac.asojuku.azcafe.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * 解答詳細テーブル モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Data
@Entity 
@Table(name="ANSWER_DETAIL_TBL")
public class AnswerDetailTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 解答詳細ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer answerDetailId;

	/** 解答テーブル. */
	private Integer answerId;

	/** ファイル名. */
	private String filename;

	/** 解答. */
	private String answer;

}
