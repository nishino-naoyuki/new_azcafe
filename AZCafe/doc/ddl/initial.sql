INSERT INTO level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)VALUES(1,'��������','',0,1,0,0,0,0,0);
INSERT INTO level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)VALUES(2,'�Ђ����','',0,5,0,0,0,0,0);
INSERT INTO level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)VALUES(3,'���K��','',900,10,0,0,0,0,0);
INSERT INTO level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)VALUES(4,'�l����','',1400,15,1,0,0,1,0);
INSERT INTO level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)VALUES(5,'��l�O��','',2500,30,1,0,0,2,0);
INSERT INTO level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)VALUES(6,'�_','',4500,50,10,10,5,3,50);
INSERT INTO level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)VALUES(7,'�X�y�V�����X�g','',0,0,0,0,0,0,60);

INSERT INTO COURSE_TBL(`course_id`,`name`,`year`)VALUES(1,'���H�w��',4);
INSERT INTO COURSE_TBL(`course_id`,`name`,`year`)VALUES(2,'���V�X�e����U��',3);
INSERT INTO COURSE_TBL(`course_id`,`name`,`year`)VALUES(3,'���V�X�e����',2);

INSERT INTO HOMEROOM_TBL(`homeroom_id`,`course_id`,`name`)VALUES(1,3,'ITA');
INSERT INTO HOMEROOM_TBL(`homeroom_id`,`course_id`,`name`)VALUES(2,3,'ITB');
INSERT INTO HOMEROOM_TBL(`homeroom_id`,`course_id`,`name`)VALUES(3,2,'ITC');
INSERT INTO HOMEROOM_TBL(`homeroom_id`,`course_id`,`name`)VALUES(4,2,'ITD');
INSERT INTO HOMEROOM_TBL(`homeroom_id`,`course_id`,`name`)VALUES(5,2,'ITE');
INSERT INTO HOMEROOM_TBL(`homeroom_id`,`course_id`,`name`)VALUES(6,2,'ITF');
INSERT INTO HOMEROOM_TBL(`homeroom_id`,`course_id`,`name`)VALUES(7,1,'ITG');
INSERT INTO HOMEROOM_TBL(`homeroom_id`,`course_id`,`name`)VALUES(8,1,'ITH');

INSERT INTO USER_TBL(`org_no`, `nick_name`, `name`, `mail`, `password`, `role`, `homeroom_id`, `grade`, `enter_year`, `introduction`, `point`, `follow_num`, `follower_num`, `good_num`, create_date,update_date) VALUES ('0000000', '����搶', '���쒼�K', 'nishino@asojuku.ac.jp', '64d691f7849b22ccb37673e9864913a9', '1', '1', '1', '2014', 'test', '0', '0', '0', '0',now(),now());