SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS ANSWER_DETAIL_TBL;
DROP TABLE IF EXISTS ANSWER_GOOD_TBL;
DROP TABLE IF EXISTS COMMENT_TBL;
DROP TABLE IF EXISTS TEST_CASE_ANSWER_TBL;
DROP TABLE IF EXISTS ANSWER_TBL;
DROP TABLE IF EXISTS ASSIGNMENT_GOOD_TBL;
DROP TABLE IF EXISTS PUBLIC_ASSIGNMENT_TBL;
DROP TABLE IF EXISTS TEST_CASE_TBL;
DROP TABLE IF EXISTS ASSIGNMENT_TBL;
DROP TABLE IF EXISTS AUTOLOGIN_TBL;
DROP TABLE IF EXISTS FOLLOW_TBL;
DROP TABLE IF EXISTS USER_TBL;
DROP TABLE IF EXISTS HOMEROOM_TBL;
DROP TABLE IF EXISTS COURSE_TBL;
DROP TABLE IF EXISTS GROUP_TBL;
DROP TABLE IF EXISTS LEVEL_TBL;
DROP TABLE IF EXISTS USER_DELETE_TBL;




/* Create Tables */

CREATE TABLE ANSWER_DETAIL_TBL
(
	answer_detail_id int NOT NULL AUTO_INCREMENT,
	answer_id int NOT NULL,
	-- ファイルアップロード時は、アップロードしたファイル名、Webテキストでの提出の時はnull
	filename varchar(512) COMMENT 'ファイルアップロード時は、アップロードしたファイル名、Webテキストでの提出の時はnull',
	answer varchar(21845) NOT NULL,
	PRIMARY KEY (answer_detail_id)
);


CREATE TABLE ANSWER_GOOD_TBL
(
	answer_good_id int NOT NULL AUTO_INCREMENT,
	answer_id int NOT NULL,
	user_id int NOT NULL,
	good_date timestamp NOT NULL,
	PRIMARY KEY (answer_good_id)
);


CREATE TABLE ANSWER_TBL
(
	answer_id int NOT NULL AUTO_INCREMENT,
	user_id int NOT NULL,
	assignment_id int NOT NULL,
	-- 採点結果
	score int NOT NULL COMMENT '採点結果',
	correct_flg int DEFAULT 0 NOT NULL,
	PRIMARY KEY (answer_id)
);


CREATE TABLE ASSIGNMENT_GOOD_TBL
(
	assignment_good_id int NOT NULL AUTO_INCREMENT,
	assignment_id int NOT NULL,
	user_id int NOT NULL,
	good_date timestamp NOT NULL,
	PRIMARY KEY (assignment_good_id)
);


CREATE TABLE ASSIGNMENT_TBL
(
	assignment_id int NOT NULL AUTO_INCREMENT,
	group_id int NOT NULL,
	-- 画面の表示されるときにグループ内の若い番号から順に表示される
	group_in_no int NOT NULL COMMENT '画面の表示されるときにグループ内の若い番号から順に表示される',
	title varchar(200) NOT NULL,
	contents longtext NOT NULL,
	-- 1～5
	-- 1:簡単
	-- 2:やや簡単
	-- 3:普通
	-- 4:やや難
	-- 5:難
	difficulty int NOT NULL COMMENT '1～5
1:簡単
2:やや簡単
3:普通
4:やや難
5:難',
	-- QUESTION_GOOD_TBLのCOUNT
	good int NOT NULL COMMENT 'QUESTION_GOOD_TBLのCOUNT',
	create_user_id int NOT NULL,
	create_date timestamp NOT NULL,
	-- 新規登録時にも入れる
	update_user_id int NOT NULL COMMENT '新規登録時にも入れる',
	-- 新規登録時にも入れる
	update_date timestamp NOT NULL COMMENT '新規登録時にも入れる',
	PRIMARY KEY (assignment_id)
);


CREATE TABLE AUTOLOGIN_TBL
(
	autologin_id int NOT NULL AUTO_INCREMENT,
	user_id int NOT NULL,
	token varchar(200) NOT NULL,
	lmit_date timestamp,
	PRIMARY KEY (autologin_id)
);


CREATE TABLE COMMENT_TBL
(
	comment_id int NOT NULL AUTO_INCREMENT,
	answer_id int NOT NULL,
	user_id int NOT NULL,
	-- コメント
	comment varchar(4000) NOT NULL COMMENT 'コメント',
	-- 登録日
	entry_date timestamp NOT NULL COMMENT '登録日',
	PRIMARY KEY (comment_id)
);


CREATE TABLE COURSE_TBL
(
	course_id int NOT NULL AUTO_INCREMENT,
	name varchar(200) NOT NULL,
	-- 卒業までの年数
	year int NOT NULL COMMENT '卒業までの年数',
	PRIMARY KEY (course_id)
);


CREATE TABLE FOLLOW_TBL
(
	-- フォローしてる本人のID
	user_id int NOT NULL COMMENT 'フォローしてる本人のID',
	-- 非フォローユーザーのID
	follew_user_id int NOT NULL COMMENT '非フォローユーザーのID',
	follow_date timestamp NOT NULL
);


CREATE TABLE GROUP_TBL
(
	group_id int NOT NULL AUTO_INCREMENT,
	name varchar(200) NOT NULL,
	entry_date timestamp NOT NULL,
	update_date timestamp NOT NULL,
	PRIMARY KEY (group_id)
);


CREATE TABLE HOMEROOM_TBL
(
	homeroom_id int NOT NULL AUTO_INCREMENT,
	course_id int NOT NULL,
	-- クラスが無い学科はＮＵＬＬ
	name varchar(100) COMMENT 'クラスが無い学科はＮＵＬＬ',
	PRIMARY KEY (homeroom_id)
);


CREATE TABLE LEVEL_TBL
(
	level_id int NOT NULL AUTO_INCREMENT,
	name varchar(100) NOT NULL,
	description varchar(2000) NOT NULL,
	PRIMARY KEY (level_id)
);


CREATE TABLE PUBLIC_ASSIGNMENT_TBL
(
	public_assignment_id int NOT NULL AUTO_INCREMENT,
	assignment_id int NOT NULL,
	homeroom_id int NOT NULL,
	-- 0:未公開
	-- 1:公開
	-- 2:必須公開
	public_state int COMMENT '0:未公開
1:公開
2:必須公開',
	-- nullの場合は即時公開
	public_start_date timestamp COMMENT 'nullの場合は即時公開',
	-- nullだと設定無し
	public_end_date timestamp COMMENT 'nullだと設定無し',
	PRIMARY KEY (public_assignment_id)
);


CREATE TABLE TEST_CASE_ANSWER_TBL
(
	testcase_answer_id int NOT NULL AUTO_INCREMENT,
	testcase_id int NOT NULL,
	-- 0:不正解
	-- 1:正解
	correctly int NOT NULL COMMENT '0:不正解
1:正解',
	user_output varchar(2000) NOT NULL,
	answer_id int NOT NULL,
	PRIMARY KEY (testcase_answer_id)
);


CREATE TABLE TEST_CASE_TBL
(
	testcase_id int NOT NULL AUTO_INCREMENT,
	assignment_id int NOT NULL,
	-- 1~
	no int NOT NULL COMMENT '1~',
	-- 無い場合はNULL
	-- 複数ある場合は改行をいれる
	input_text varchar(2000) COMMENT '無い場合はNULL
複数ある場合は改行をいれる',
	-- テストケースの答え
	-- 実行結果とこの項目が一致するかどうかで正解かどうかを判定する
	output_txt varchar(2000) NOT NULL COMMENT 'テストケースの答え
実行結果とこの項目が一致するかどうかで正解かどうかを判定する',
	PRIMARY KEY (testcase_id)
);


CREATE TABLE USER_DELETE_TBL
(
	user_id int NOT NULL,
	-- 学生の場合は学籍番号
	-- 教員の場合は社員番号
	org_no varchar(10) NOT NULL COMMENT '学生の場合は学籍番号
教員の場合は社員番号',
	nick_name varchar(100) NOT NULL,
	name varchar(100) NOT NULL,
	-- ログインアカウントでもある
	mail varchar(256) NOT NULL COMMENT 'ログインアカウントでもある',
	-- パスワード（ハッシュ）
	-- ソルト＋メアド＋パスワード＋ソルト
	-- のハッシュ値
	password varchar(100) NOT NULL COMMENT 'パスワード（ハッシュ）
ソルト＋メアド＋パスワード＋ソルト
のハッシュ値',
	-- ０：学生
	-- １：教員
	role int DEFAULT 0 NOT NULL COMMENT '０：学生
１：教員',
	homeroom_id int NOT NULL,
	-- 学年
	grade int NOT NULL COMMENT '学年',
	-- 入学年度
	-- 教員の場合は入社年度（教員の場合みないので適当でＯＫ）
	enter_year int NOT NULL COMMENT '入学年度
教員の場合は入社年度（教員の場合みないので適当でＯＫ）',
	-- 自己紹介文
	introduction varchar(3000) NOT NULL COMMENT '自己紹介文',
	PRIMARY KEY (user_id),
	UNIQUE (org_no)
);


CREATE TABLE USER_TBL
(
	user_id int NOT NULL AUTO_INCREMENT,
	-- 学生の場合は学籍番号
	-- 教員の場合は社員番号
	org_no varchar(10) NOT NULL COMMENT '学生の場合は学籍番号
教員の場合は社員番号',
	nick_name varchar(100) NOT NULL,
	name varchar(100) NOT NULL,
	-- ログインアカウントでもある
	mail varchar(256) NOT NULL COMMENT 'ログインアカウントでもある',
	-- パスワード（ハッシュ）
	-- ソルト＋メアド＋パスワード＋ソルト
	-- のハッシュ値
	password varchar(100) NOT NULL COMMENT 'パスワード（ハッシュ）
ソルト＋メアド＋パスワード＋ソルト
のハッシュ値',
	-- ０：学生
	-- １：教員
	role int DEFAULT 0 NOT NULL COMMENT '０：学生
１：教員',
	homeroom_id int NOT NULL,
	-- 学年
	grade int NOT NULL COMMENT '学年',
	-- 入学年度
	-- 教員の場合は入社年度（教員の場合みないので適当でＯＫ）
	enter_year int NOT NULL COMMENT '入学年度
教員の場合は入社年度（教員の場合みないので適当でＯＫ）',
	-- 自己紹介文
	introduction varchar(3000) NOT NULL COMMENT '自己紹介文',
	level_id int NOT NULL,
	-- 画像のファイル名
	avater varchar(2000) COMMENT '画像のファイル名',
	-- 問題を解くたびに更新する
	-- ランキングのもととなるポイント
	point int NOT NULL COMMENT '問題を解くたびに更新する
ランキングのもととなるポイント',
	follow_num int NOT NULL,
	follower_num int NOT NULL,
	good_num int NOT NULL,
	create_date timestamp NOT NULL,
	update_date timestamp NOT NULL,
	PRIMARY KEY (user_id),
	UNIQUE (org_no)
);



/* Create Foreign Keys */

ALTER TABLE ANSWER_DETAIL_TBL
	ADD FOREIGN KEY (answer_id)
	REFERENCES ANSWER_TBL (answer_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ANSWER_GOOD_TBL
	ADD FOREIGN KEY (answer_id)
	REFERENCES ANSWER_TBL (answer_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE COMMENT_TBL
	ADD FOREIGN KEY (answer_id)
	REFERENCES ANSWER_TBL (answer_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TEST_CASE_ANSWER_TBL
	ADD FOREIGN KEY (answer_id)
	REFERENCES ANSWER_TBL (answer_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ANSWER_TBL
	ADD FOREIGN KEY (assignment_id)
	REFERENCES ASSIGNMENT_TBL (assignment_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ASSIGNMENT_GOOD_TBL
	ADD FOREIGN KEY (assignment_id)
	REFERENCES ASSIGNMENT_TBL (assignment_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE PUBLIC_ASSIGNMENT_TBL
	ADD FOREIGN KEY (assignment_id)
	REFERENCES ASSIGNMENT_TBL (assignment_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TEST_CASE_TBL
	ADD FOREIGN KEY (assignment_id)
	REFERENCES ASSIGNMENT_TBL (assignment_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE HOMEROOM_TBL
	ADD FOREIGN KEY (course_id)
	REFERENCES COURSE_TBL (course_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ASSIGNMENT_TBL
	ADD FOREIGN KEY (group_id)
	REFERENCES GROUP_TBL (group_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE PUBLIC_ASSIGNMENT_TBL
	ADD FOREIGN KEY (homeroom_id)
	REFERENCES HOMEROOM_TBL (homeroom_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE USER_TBL
	ADD FOREIGN KEY (homeroom_id)
	REFERENCES HOMEROOM_TBL (homeroom_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE USER_TBL
	ADD FOREIGN KEY (level_id)
	REFERENCES LEVEL_TBL (level_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE TEST_CASE_ANSWER_TBL
	ADD FOREIGN KEY (testcase_id)
	REFERENCES TEST_CASE_TBL (testcase_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ANSWER_GOOD_TBL
	ADD FOREIGN KEY (user_id)
	REFERENCES USER_TBL (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ANSWER_TBL
	ADD FOREIGN KEY (user_id)
	REFERENCES USER_TBL (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE ASSIGNMENT_GOOD_TBL
	ADD FOREIGN KEY (user_id)
	REFERENCES USER_TBL (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE AUTOLOGIN_TBL
	ADD FOREIGN KEY (user_id)
	REFERENCES USER_TBL (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE COMMENT_TBL
	ADD FOREIGN KEY (user_id)
	REFERENCES USER_TBL (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE FOLLOW_TBL
	ADD FOREIGN KEY (follew_user_id)
	REFERENCES USER_TBL (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE FOLLOW_TBL
	ADD FOREIGN KEY (user_id)
	REFERENCES USER_TBL (user_id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



