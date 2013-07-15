delimiter /
TRUNCATE TABLE PROTOCOL_STATUS
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('100','Pending/In Progress','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('101','Submitted to IRB','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('102','Specific Minor Revisions Required','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('103','Deferred','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('104','Substantive Revisions Required','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('105','Amendment in Progress','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('106','Renewal in Progress','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('200','Active - Open to Enrollment','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('201','Active - Closed to Enrollment','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('202','Active - Data Analysis Only','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('203','Exempt','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('300','Closed Administratively for lack of response','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('301','Closed by Investigator','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('302','Suspended by Investigator','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('303','Deleted','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('304','Withdrawn','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('305','Expired','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('306','Disapproved','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('307','Terminated by IRB','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('308','Suspended by IRB','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('309','Not Human Subjects Research','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('310','IRB review not required','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('311','Suspended by DSMB','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('400','Amendment Incorporated into Protocol','admin',NOW(),UUID(),1)
/
INSERT INTO PROTOCOL_STATUS (PROTOCOL_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('401','Renewal Incorporated into Protocol','admin',NOW(),UUID(),1)
/
delimiter ;
