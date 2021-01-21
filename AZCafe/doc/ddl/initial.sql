INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('1', '未経験', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('2', 'かけだし', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('3', '見習い', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('4', '人並み', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('5', '一人前', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('6', 'エース', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('7', '一流', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('8', 'カリスマ', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('9', 'キング', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('10', '神', ' ');

INSERT INTO course_tbl(`course_id`,`name`,`year`)VALUES(1,'情報工学科',4);
INSERT INTO course_tbl(`course_id`,`name`,`year`)VALUES(2,'情報システム専攻科',3);
INSERT INTO course_tbl(`course_id`,`name`,`year`)VALUES(3,'情報システム科',2);

INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(1,3,'ITA');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(2,3,'ITB');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(3,2,'ITC');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(4,2,'ITD');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(5,2,'ITE');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(6,2,'ITF');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(7,1,'ITG');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(8,1,'ITH');

INSERT INTO user_tbl(`org_no`, `nick_name`, `name`, `mail`, `password`, `role`, `homeroom_id`, `grade`, `enter_year`, `introduction`, `level_id`, `point`, `follow_num`, `follower_num`, `good_num`, create_date,update_date) VALUES ('0000000', '西野先生', '西野直幸', 'nishino@asojuku.ac.jp', '64d691f7849b22ccb37673e9864913a9', '1', '1', '1', '2014', 'test', '1', '0', '0', '0', '0',now(),now());