INSERT INTO KRIM_PERM_T (PERM_ID,OBJ_ID,VER_NBR,PERM_TMPL_ID,NMSPC_CD,NM,DESC_TXT,ACTV_IND) VALUES ('1226',SYS_GUID(),1,'1','KC-M','Create Valid Rate','Create a Valid Rates entry','Y');
INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID,OBJ_ID,VER_NBR,PERM_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL) VALUES ('1299',SYS_GUID(),1,'1226','18','18','OSP Administrator');
INSERT INTO krim_role_perm_t (ROLE_PERM_ID,OBJ_ID,VER_NBR,ROLE_ID,PERM_ID,ACTV_IND) VALUES ('10222',SYS_GUID(),1,'1114','1226','Y');
INSERT INTO KRIM_ROLE_MBR_ATTR_DATA_T (ATTR_DATA_ID,OBJ_ID,VER_NBR,ROLE_MBR_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL) VALUES ('1303',SYS_GUID(),1,'1114','18','18','OSP Administrator');
