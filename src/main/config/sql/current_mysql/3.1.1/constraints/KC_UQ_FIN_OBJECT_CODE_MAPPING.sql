DELIMITER /

ALTER TABLE FIN_OBJECT_CODE_MAPPING
    ADD CONSTRAINT FIN_OBJECT_CODE_U1
UNIQUE (RATE_CLASS_CODE,RATE_TYPE_CODE,UNIT_NUMBER, ACTIVITY_TYPE_CODE)
/

DELIMITER ;
