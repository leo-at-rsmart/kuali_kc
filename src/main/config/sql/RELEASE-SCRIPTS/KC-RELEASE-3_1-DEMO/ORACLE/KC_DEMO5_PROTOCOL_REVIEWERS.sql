INSERT INTO PROTOCOL_REVIEWERS (NON_EMPLOYEE_FLAG,OBJ_ID,PERSON_ID,PROTOCOL_ID,PROTOCOL_NUMBER,PROTOCOL_REVIEWER_ID,REVIEWER_TYPE_CODE,SEQUENCE_NUMBER,SUBMISSION_ID_FK,SUBMISSION_NUMBER,UPDATE_TIMESTAMP,UPDATE_USER,VER_NBR)
  VALUES ('N',SYS_GUID(),'10000000005',(SELECT PROTOCOL_ID FROM PROTOCOL WHERE PROTOCOL_NUMBER = '1009000019'),'1009000019',SEQ_PROTOCOL_ID.NEXTVAL,'1',0,(SELECT SUBMISSION_ID FROM PROTOCOL_SUBMISSION WHERE PROTOCOL_NUMBER = '1009000019'),1,TO_DATE( '20100923223606', 'YYYYMMDDHH24MISS' ),'quickstart',0)
/
INSERT INTO PROTOCOL_REVIEWERS (NON_EMPLOYEE_FLAG,OBJ_ID,PERSON_ID,PROTOCOL_ID,PROTOCOL_NUMBER,PROTOCOL_REVIEWER_ID,REVIEWER_TYPE_CODE,SEQUENCE_NUMBER,SUBMISSION_ID_FK,SUBMISSION_NUMBER,UPDATE_TIMESTAMP,UPDATE_USER,VER_NBR)
  VALUES ('N',SYS_GUID(),'10000000005',(SELECT PROTOCOL_ID FROM PROTOCOL WHERE PROTOCOL_NUMBER = '1009000028' AND SEQUENCE_NUMBER = 0),'1009000028',SEQ_PROTOCOL_ID.NEXTVAL,'1',0,(SELECT SUBMISSION_ID FROM PROTOCOL_SUBMISSION WHERE PROTOCOL_NUMBER = '1009000028' AND SEQUENCE_NUMBER = 1),1,TO_DATE( '20100923230206', 'YYYYMMDDHH24MISS' ),'quickstart',0)
/
INSERT INTO PROTOCOL_REVIEWERS (NON_EMPLOYEE_FLAG,OBJ_ID,PERSON_ID,PROTOCOL_ID,PROTOCOL_NUMBER,PROTOCOL_REVIEWER_ID,REVIEWER_TYPE_CODE,SEQUENCE_NUMBER,SUBMISSION_ID_FK,SUBMISSION_NUMBER,UPDATE_TIMESTAMP,UPDATE_USER,VER_NBR)
  VALUES ('N',SYS_GUID(),'10000000005',(SELECT PROTOCOL_ID FROM PROTOCOL WHERE PROTOCOL_NUMBER = '1009000049' AND SEQUENCE_NUMBER = 0),'1009000049',SEQ_PROTOCOL_ID.NEXTVAL,'1',0,(SELECT SUBMISSION_ID FROM PROTOCOL_SUBMISSION WHERE PROTOCOL_NUMBER = '1009000049' AND SEQUENCE_NUMBER = 1),1,TO_DATE( '20100923230843', 'YYYYMMDDHH24MISS' ),'quickstart',0)
/
