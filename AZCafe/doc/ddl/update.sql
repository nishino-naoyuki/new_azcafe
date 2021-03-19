ALTER TABLE `homeroom_tbl` CHANGE `homeroom_id` `homeroom_id` INT NOT NULL;
ALTER TABLE `course_tbl` CHANGE `course_id` `course_id` INT NOT NULL;
update user_tbl set org_no="" where role=1;