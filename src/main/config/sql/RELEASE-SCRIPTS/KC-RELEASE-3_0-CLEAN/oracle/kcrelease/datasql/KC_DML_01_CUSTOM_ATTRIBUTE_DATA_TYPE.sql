TRUNCATE TABLE CUSTOM_ATTRIBUTE_DATA_TYPE DROP STORAGE
/
INSERT INTO CUSTOM_ATTRIBUTE_DATA_TYPE (DATA_TYPE_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('1','String','admin',SYSDATE,SYS_GUID(),1)
/
INSERT INTO CUSTOM_ATTRIBUTE_DATA_TYPE (DATA_TYPE_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('2','Number','admin',SYSDATE,SYS_GUID(),1)
/
INSERT INTO CUSTOM_ATTRIBUTE_DATA_TYPE (DATA_TYPE_CODE,DESCRIPTION,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('3','Date','admin',SYSDATE,SYS_GUID(),1)
/
