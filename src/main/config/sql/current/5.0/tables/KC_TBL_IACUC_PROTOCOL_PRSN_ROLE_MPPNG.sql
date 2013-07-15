CREATE TABLE IACUC_PROTOCOL_PRSN_ROLE_MPPNG (
    ROLE_MAPPING_ID NUMBER(12,0) NOT NULL,
    SOURCE_ROLE_ID VARCHAR(12) NOT NULL,
    TARGET_ROLE_ID VARCHAR(12) NOT NULL,
    UPDATE_TIMESTAMP DATE NOT NULL, 
    UPDATE_USER VARCHAR2(60) NOT NULL, 
    VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL,     
    OBJ_ID VARCHAR2(36) NOT NULL
)
/
ALTER TABLE IACUC_PROTOCOL_PRSN_ROLE_MPPNG 
ADD CONSTRAINT PK_IACUC_PRTCL_PRSN_ROLE_MPPNG
PRIMARY KEY (ROLE_MAPPING_ID) 
/
