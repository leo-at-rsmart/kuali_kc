--create role types
INSERT INTO KRIM_TYP_T (KIM_TYP_ID, OBJ_ID, VER_NBR, NM, SRVC_NM, ACTV_IND, NMSPC_CD)
  VALUES( KRIM_TYP_ID_BS_S.NEXTVAL, SYS_GUID(), 1, 'Role', 'organizationCorrespondentDerivedRoleType', 'Y', 'KC-PROTOCOL') 
/
--create new roles
INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND, LAST_UPDT_DT)
VALUES (KRIM_ROLE_PERM_ID_BS_S.NEXTVAL, SYS_GUID(), 1, 'Organization Correspondent', 'KC-PROTOCOL', 'The Organization Correspondent role', (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE SRVC_NM = 'unitCorrespondentDerivedRoleType'), 'Y', SYSDATE)
/
COMMIT
/
