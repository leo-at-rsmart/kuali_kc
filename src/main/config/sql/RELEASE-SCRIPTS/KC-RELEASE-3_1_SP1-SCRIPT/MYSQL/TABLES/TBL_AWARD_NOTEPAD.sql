delimiter /
--Create 'CREATE_USER' record
ALTER TABLE "AWARD_NOTEPAD"
	ADD ( "CREATE_USER" VARCHAR2(60) NULL )
/
--Populate new field with existing data
UPDATE AWARD_NOTEPAD
SET CREATE_USER  = UPDATE_USER
/
COMMIT
/
--make new field non-nullable
ALTER TABLE "AWARD_NOTEPAD" MODIFY ( "CREATE_USER" VARCHAR2(60) NOT NULL )
/
delimiter ;