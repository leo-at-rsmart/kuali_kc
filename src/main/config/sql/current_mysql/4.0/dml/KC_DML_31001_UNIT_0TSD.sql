DELIMITER /
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('BL-BL','IU-UNIV','BLOOMINGTON CAMPUS','admin',NOW(),UUID(),1)
/
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('BL-IIDC','BL-RCEN','IND INST ON DISABILITY/COMMNTY','admin',NOW(),UUID(),1)
/
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('BL-RCEN','BL-RUGS','RESEARCH CENTERS','admin',NOW(),UUID(),1)
/
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('BL-RUGS','BL-BL','OFFICE OF VP FOR RESEARCH','admin',NOW(),UUID(),1)
/
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('IN-CARD','IN-MDEP','CARDIOLOGY','admin',NOW(),UUID(),1)
/
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('IN-CARR','IN-CARD','CARDIOLOGY RECHARGE CTR','admin',NOW(),UUID(),1)
/
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('IN-IN','IU-UNIV','IND UNIV-PURDUE UNIV INDPLS','admin',NOW(),UUID(),1)
/
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('IN-MDEP','IN-MED','MEDICINE DEPT','admin',NOW(),UUID(),1)
/
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('IN-MED','IN-IN','SCHOOL OF MEDICINE','admin',NOW(),UUID(),1)
/
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('IN-PED','IN-MED','PEDIATRICS','admin',NOW(),UUID(),1)
/
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('IN-PERS','IN-PED','PED-EMERGENCY ROOM SERVICES','admin',NOW(),UUID(),1)
/
INSERT INTO UNIT (UNIT_NUMBER,PARENT_UNIT_NUMBER,UNIT_NAME,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES ('IU-UNIV','000001','UNIVERSITY LEVEL','admin',NOW(),UUID(),1)
/
DELIMITER ;
