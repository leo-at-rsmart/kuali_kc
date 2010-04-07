-- Table Script 
CREATE TABLE BATCH_CORRESPONDENCE_DETAIL ( 
    BATCH_CORRESPONDENCE_DETAIL_ID NUMBER(12,0) NOT NULL, 
    BATCH_CORRESPONDENCE_TYPE_CODE VARCHAR2(3) NOT NULL, 
    -- The CORRESPONDENCE_NUMBER is an artifact from the Coeus system.
    -- While it is not used it remains for backward equivalency.
    CORRESPONDENCE_NUMBER NUMBER(3,0), 
    PROTO_CORRESP_TYPE_CODE VARCHAR2(3) NOT NULL, 
    NO_OF_DAYS_TILL_NEXT NUMBER(3,0) NOT NULL, 
    UPDATE_TIMESTAMP DATE NOT NULL, 
    UPDATE_USER VARCHAR2(60) NOT NULL, 
    VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL, 
    OBJ_ID VARCHAR2(36) DEFAULT SYS_GUID() NOT NULL);


-- Primary Key Constraint 
ALTER TABLE BATCH_CORRESPONDENCE_DETAIL 
ADD CONSTRAINT PK_BATCH_CORRESPONDENCE_DETAIL 
PRIMARY KEY (BATCH_CORRESPONDENCE_DETAIL_ID);

-- *************** MODIFIED PRIMARY KEY COLUMN - Introduced new primary key for existing composite key ************ 
-- ALTER TABLE BATCH_CORRESPONDENCE_DETAIL 
-- ADD CONSTRAINT PK_BATCH_CORRESPONDENCE_DETAIL 
-- PRIMARY KEY (BATCH_CORRESPONDENCE_TYPE_CODE, CORRESPONDENCE_NUMBER);
-- *************** MODIFIED PRIMARY KEY COLUMN - Introduced new primary key for existing composite key ************ 
 

-- *************** UNIQUE CONSTRAINT DEFINED FOR COMPOSITE KEY COLUMNS ************ 
--ALTER TABLE BATCH_CORRESPONDENCE_DETAIL 
--ADD CONSTRAINT UQ_BATCH_CORRESPONDENCE_DETAIL 
--UNIQUE (BATCH_CORRESPONDENCE_TYPE_CODE, CORRESPONDENCE_NUMBER);

-- Foreign Key Constraint(s)
ALTER TABLE BATCH_CORRESPONDENCE_DETAIL 
ADD CONSTRAINT FK_BATCH_CORRESPONDENCE_DETAIL 
FOREIGN KEY (PROTO_CORRESP_TYPE_CODE) 
REFERENCES PROTO_CORRESP_TYPE (PROTO_CORRESP_TYPE_CODE);

