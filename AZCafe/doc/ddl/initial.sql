INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('1', '���o��', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('2', '��������', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('3', '���K��', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('4', '�l����', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('5', '��l�O', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('6', '�G�[�X', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('7', '�ꗬ', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('8', '�J���X�}', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('9', '�L���O', ' ');
INSERT INTO level_tbl (`level_id`, `name`, `description`) VALUES ('10', '�_', ' ');

INSERT INTO course_tbl(`course_id`,`name`,`year`)VALUES(1,'���H�w��',4);
INSERT INTO course_tbl(`course_id`,`name`,`year`)VALUES(2,'���V�X�e����U��',3);
INSERT INTO course_tbl(`course_id`,`name`,`year`)VALUES(3,'���V�X�e����',2);

INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(1,3,'ITA');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(2,3,'ITB');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(3,2,'ITC');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(4,2,'ITD');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(5,2,'ITE');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(6,2,'ITF');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(7,1,'ITG');
INSERT INTO homeroom_tbl(`homeroom_id`,`course_id`,`name`)VALUES(8,1,'ITH');

INSERT INTO user_tbl(`org_no`, `nick_name`, `name`, `mail`, `password`, `role`, `homeroom_id`, `grade`, `enter_year`, `introduction`, `level_id`, `point`, `follow_num`, `follower_num`, `good_num`, create_date,update_date) VALUES ('0000000', '����搶', '���쒼�K', 'nishino@asojuku.ac.jp', '64d691f7849b22ccb37673e9864913a9', '1', '1', '1', '2014', 'test', '1', '0', '0', '0', '0',now(),now());