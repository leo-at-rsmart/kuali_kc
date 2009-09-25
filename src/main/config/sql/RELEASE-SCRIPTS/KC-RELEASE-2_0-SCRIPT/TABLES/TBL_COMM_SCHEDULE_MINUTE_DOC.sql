-- Table Script  
CREATE TABLE COMM_SCHEDULE_MINUTE_DOC (
    COMM_SCHEDULE_MINUTE_DOC_ID NUMBER (12, 0) NOT NULL, 
    SCHEDULE_ID_FK NUMBER (12, 0) NOT NULL, 
    MINUTE_NUMBER NUMBER (4, 0) NOT NULL, 
    MINUTE_NAME VARCHAR2 (200) NOT NULL, 
    PDF_STORE BLOB DEFAULT EMPTY_BLOB () , 
    CREATE_TIMESTAMP DATE NOT NULL, 
    CREATE_USER VARCHAR2 (8) NOT NULL, 
    UPDATE_TIMESTAMP DATE NOT NULL, 
    UPDATE_USER VARCHAR2 (60) NOT NULL, 
    VER_NBR NUMBER (8, 0) DEFAULT 1 NOT NULL, 
    OBJ_ID VARCHAR2 (36) DEFAULT SYS_GUID () NOT NULL) ;

-- Primary Key Constraint  
ALTER TABLE COMM_SCHEDULE_MINUTE_DOC 
    ADD CONSTRAINT PK_COMM_SCHEDULE_MINUTE_DOC 
            PRIMARY KEY (COMM_SCHEDULE_MINUTE_DOC_ID) ;

-- *************** UNIQUE CONSTRAINT DEFINED FOR COMPOSITE KEY COLUMNS ************  
ALTER TABLE COMM_SCHEDULE_MINUTE_DOC 
    ADD CONSTRAINT UQ_COMM_SCHEDULE_MINUTE_DOC 
            UNIQUE (SCHEDULE_ID_FK, MINUTE_NUMBER) ;