INSERT IGNORE INTO KRIM_ENTITY_T (ACTV_IND,ENTITY_ID,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
VALUES ('Y','1131',NOW(),UUID(),1);

INSERT IGNORE INTO KRIM_PRNCPL_T (ACTV_IND,ENTITY_ID,LAST_UPDT_DT,OBJ_ID,PRNCPL_ID,PRNCPL_NM,VER_NBR) 
VALUES ('Y','1131', NOW(),UUID(),'notsys','notsys',1);

INSERT IGNORE INTO KRIM_ENTITY_ENT_TYP_T(ENTITY_ID, ENT_TYP_CD, ACTV_IND, LAST_UPDT_DT, VER_NBR, OBJ_ID) 
VALUES ((SELECT ENTITY_ID FROM KRIM_PRNCPL_T WHERE UPPER(PRNCPL_NM) = 'NOTSYS'), 'PERSON', 'Y', NOW(), 1, UUID());

COMMIT;
