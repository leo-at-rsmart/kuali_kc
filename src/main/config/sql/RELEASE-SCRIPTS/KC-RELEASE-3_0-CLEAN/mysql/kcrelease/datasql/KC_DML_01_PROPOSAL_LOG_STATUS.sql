delimiter /
TRUNCATE TABLE PROPOSAL_LOG_STATUS
/
INSERT INTO PROPOSAL_LOG_STATUS (PROPOSAL_LOG_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('1','Pending','admin',NOW(),UUID(),1)
/
INSERT INTO PROPOSAL_LOG_STATUS (PROPOSAL_LOG_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('2','Merged','admin',NOW(),UUID(),1)
/
INSERT INTO PROPOSAL_LOG_STATUS (PROPOSAL_LOG_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('3','Submitted','admin',NOW(),UUID(),1)
/
INSERT INTO PROPOSAL_LOG_STATUS (PROPOSAL_LOG_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('4','Void','admin',NOW(),UUID(),1)
/
INSERT INTO PROPOSAL_LOG_STATUS (PROPOSAL_LOG_STATUS_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('5','Temporary','admin',NOW(),UUID(),1)
/
delimiter ;
