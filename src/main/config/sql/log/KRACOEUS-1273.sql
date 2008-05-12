CREATE TABLE SPONSOR_FORMS
(
  SPONSOR_CODE      CHAR(6)                NOT NULL,
  PACKAGE_NUMBER    NUMBER(3)                   NOT NULL,
  PACKAGE_NAME      VARCHAR2(200)          NOT NULL,
  UPDATE_TIMESTAMP  DATE                        NOT NULL,
  UPDATE_USER       VARCHAR2(8)            NOT NULL
);

CREATE OR REPLACE VIEW OSP$SPONSOR_FORMS AS
SELECT  SPONSOR_CODE, PACKAGE_NUMBER,  PACKAGE_NAME,
  UPDATE_TIMESTAMP, UPDATE_USER
FROM SPONSOR_FORMS;


CREATE TABLE SPONSOR_FORM_TEMPLATES
(
  SPONSOR_CODE      CHAR(6)                NOT NULL,
  PACKAGE_NUMBER    NUMBER(3)                   NOT NULL,
  PAGE_NUMBER       NUMBER(3)                   NOT NULL,
  PAGE_DESCRIPTION  VARCHAR2(200)          NOT NULL,
  FORM_TEMPLATE     BLOB                        DEFAULT EMPTY_BLOB(),
  UPDATE_TIMESTAMP  DATE                        NOT NULL,
  UPDATE_USER       VARCHAR2(60)            NOT NULL
);

CREATE OR REPLACE VIEW OSP$SPONSOR_FORM_TEMPLATES AS
SELECT SPONSOR_CODE,  PACKAGE_NUMBER,  PAGE_NUMBER, PAGE_DESCRIPTION,
  FORM_TEMPLATE, UPDATE_TIMESTAMP, UPDATE_USER
FROM SPONSOR_FORM_TEMPLATES;

