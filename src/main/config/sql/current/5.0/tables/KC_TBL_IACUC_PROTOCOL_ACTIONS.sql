CREATE TABLE IACUC_PROTOCOL_ACTIONS ( 
    IACUC_PROTOCOL_ACTIONS_ID NUMBER(12,0) NOT NULL, 
    PROTOCOL_NUMBER VARCHAR2(20) NOT NULL, 
    SEQUENCE_NUMBER NUMBER(4,0) NOT NULL, 
    VERSION_NUMBER NUMBER(4,0), 
    ACTION_ID NUMBER(6,0) NOT NULL, 
    PROTOCOL_ACTION_TYPE_CODE VARCHAR2(3) NOT NULL, 
    SUBMISSION_NUMBER NUMBER(4,0), 
    COMMENTS VARCHAR2(2000), 
    UPDATE_TIMESTAMP DATE NOT NULL, 
    UPDATE_USER VARCHAR2(60) NOT NULL, 
    ACTION_DATE DATE DEFAULT NULL, 
    VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL, 
    OBJ_ID VARCHAR2(36) NOT NULL)
/
ALTER TABLE IACUC_PROTOCOL_ACTIONS 
ADD CONSTRAINT PK_IACUC_PROTOCOL_ACTIONS 
PRIMARY KEY (IACUC_PROTOCOL_ACTIONS_ID)
/