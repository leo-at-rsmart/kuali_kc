DELIMITER /

CREATE TABLE CONTACT_USAGE (
   CONTACT_USAGE_ID  DECIMAL(12,0) NOT NULL,
   CONTACT_TYPE_CODE VARCHAR(3) COLLATE utf8_bin NOT NULL,
   MODULE_CODE       DECIMAL(3,0) NOT NULL,
   UPDATE_TIMESTAMP  DATETIME NOT NULL,
   UPDATE_USER VARCHAR(60) NOT NULL, 
   VER_NBR DECIMAL(8,0) DEFAULT 1 NOT NULL,
   OBJ_ID VARCHAR(36) NOT NULL)
/

ALTER TABLE CONTACT_USAGE
ADD CONSTRAINT CONTACT_USAGE_ID_PK
PRIMARY KEY (CONTACT_USAGE_ID)
/

ALTER TABLE CONTACT_USAGE
ADD CONSTRAINT CONTACT_USAGE_UK
UNIQUE (CONTACT_TYPE_CODE, MODULE_CODE)
/

DELIMITER ;
