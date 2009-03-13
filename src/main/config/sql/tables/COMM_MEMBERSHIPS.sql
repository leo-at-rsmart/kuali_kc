-- Table Script
CREATE TABLE COMM_MEMBERSHIPS ( 
    COMM_MEMBERSHIP_ID NUMBER(12,0) NOT NULL,
    ID NUMBER(12,0) NOT NULL,
    COMMITTEE_ID VARCHAR2(15) NOT NULL,
    PERSON_ID VARCHAR2(9) NOT NULL,
    ROLODEX_ID NUMBER(12,0) NULL, 
    PERSON_NAME VARCHAR2(90) NOT NULL, 
    MEMBERSHIP_ID VARCHAR2(10) NOT NULL,
    SEQUENCE_NUMBER NUMBER(4,0) NOT NULL,
    PAID_MEMBER_FLAG VARCHAR2(1) NOT NULL,
    TERM_START_DATE DATE NOT NULL,
    TERM_END_DATE DATE,
    MEMBERSHIP_TYPE_CODE VARCHAR2(3) NOT NULL,
    COMMENTS CLOB,
    CONTACT_NOTES CLOB,
    TRAINING_NOTES CLOB,
    UPDATE_TIMESTAMP DATE, 
    UPDATE_USER VARCHAR2(60), 
    VER_NBR NUMBER(8,0) DEFAULT 1 NOT NULL, 
    OBJ_ID VARCHAR2(36) DEFAULT SYS_GUID() NOT NULL);

-- Primary Key Constraint 
ALTER TABLE COMM_MEMBERSHIPS 
ADD CONSTRAINT PK_COMM_MEMBERSHIPS 
PRIMARY KEY (COMM_MEMBERSHIP_ID);
 

-- Unique Constrains 
ALTER TABLE COMM_MEMBERSHIPS 
ADD CONSTRAINT UQ_COMM_MEMBERSHIPS 
UNIQUE (MEMBERSHIP_ID, SEQUENCE_NUMBER);

-- Foreign Key Constraint(s)  
ALTER TABLE COMM_MEMBERSHIPS 
ADD CONSTRAINT FK_COMM_MEMBERSHIPS 
FOREIGN KEY (ID) 
REFERENCES COMMITTEE (ID);

ALTER TABLE COMM_MEMBERSHIPS 
ADD CONSTRAINT FK_COMM_MEMBERSHIPS_2 
FOREIGN KEY (MEMBERSHIP_TYPE_CODE) 
REFERENCES COMM_MEMBERSHIP_TYPE (MEMBERSHIP_TYPE_CODE);

