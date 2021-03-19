insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(1,'かけだし','',0,1,0,0,0,0,0);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(2,'ひよっこ','',0,5,0,0,0,0,0);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(3,'見習い','',900,10,0,0,0,0,0);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(4,'人並み','',1400,15,1,0,0,1,0);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(5,'一人前み','',2500,30,1,0,0,2,0);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(6,'神','',4500,50,10,10,5,3,50);
insert into level_tbl(`level_id`,`name`,`description`,`point`,`answer`,`follower`,`good`,`comment`,`level`,`skill_point`)values(7,'スペシャリスト','',0,0,0,0,0,0,60);

insert into course_tbl(`course_id`,`name`,`year`)values(1,'情報工学科',4);
insert into course_tbl(`course_id`,`name`,`year`)values(2,'情報システム専攻科',3);
insert into course_tbl(`course_id`,`name`,`year`)values(3,'情報システム科',2);
insert into course_tbl(`course_id`,`name`,`year`)values(0,'その他',0);

insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(1,3,'ita');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(2,3,'itb');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(3,2,'itc');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(4,2,'itd');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(5,2,'ite');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(6,2,'itf');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(7,1,'itg');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(8,1,'ith');
insert into homeroom_tbl(`homeroom_id`,`course_id`,`name`)values(0,0,'アドバンス');

insert into user_tbl(`org_no`, `nick_name`, `name`, `mail`, `password`, `role`, `homeroom_id`, `grade`, `enter_year`, `introduction`, `point`, `follow_num`, `follower_num`, `good_num`, create_date,update_date) values ('0000000', '西野先生', '西野直幸', 'nishino@asojuku.ac.jp', '64d691f7849b22ccb37673e9864913a9', '1', '1', '1', '2014', 'test', '0', '0', '0', '0',now(),now());