ALTER TABLE CATEGORY ADD can_be_positive bool default true;
ALTER TABLE CATEGORY ADD can_be_negative bool default true;
ALTER TABLE TRANSACTION ADD scheduled bool default false;