
CREATE OR REPLACE VIEW OSP$ABSTRACT_TYPE ( ABSTRACT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER) 
	   AS select ABSTRACT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER
	from ABSTRACT_TYPE;
