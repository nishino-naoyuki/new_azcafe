insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(1,'��������','',0,1,0,0,0,0,0);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(2,'�Ђ����','',0,5,0,0,0,0,0);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(3,'���K��','',900,10,0,0,0,0,0);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(4,'�l����','',1400,15,1,0,0,1,0);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(5,'��l�O��','',2500,30,1,0,0,2,0);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(6,'�_','',4500,50,10,10,5,3,50);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(7,'�X�y�V�����X�g','',0,0,0,0,0,0,60);

insert into course_tbl(`course_id`,`name`,`year`)values(1,'���H�w��',4);
insert into course_tbl(`course_id`,`name`,`year`)values(2,'���V�X�e����U��',3);
insert into course_tbl(`course_id`,`name`,`year`)values(3,'���V�X�e����',2);
insert into course_tbl(`course_id`,`name`,`year`)values(0,'���̑�',0);

insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(1,3,'ita');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(2,3,'itb');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(3,2,'itc');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(4,2,'itd');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(5,2,'ite');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(6,2,'itf');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(7,1,'itg');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(8,1,'ith');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(0,0,'�A�h�o���X');

insert into user_tbl(`org_no`, `nick_name`, `name`, `mail`, `password`, `role`, `homeroom_id`, `grade`, `enter_year`, `introduction`, `point`, `follow_num`, `follower_num`, `good_num`, create_date,update_date) values ('0000000', '����搶', '���쒼�K', 'nishino@asojuku.ac.jp', '64d691f7849b22ccb37673e9864913a9', '1', '1', '1', '2014', 'test', '0', '0', '0', '0',now(),now());