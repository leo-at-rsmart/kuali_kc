DELIMITER /
/
INSERT INTO NEGOTIATION_STATUS_S VALUES(NULL)
/
-- Insert negotiation statuses
INSERT INTO NEGOTIATION_STATUS (NEGOTIATION_STATUS_ID, NEGOTIATION_STATUS_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID, ACTV_IND, VER_NBR)
VALUES ((SELECT (MAX(ID)) FROM NEGOTIATION_STATUS_S), 'IP', 'In Progress', NOW(), 'admin', UUID(), 'Y', 1)
/
INSERT INTO NEGOTIATION_STATUS_S VALUES(NULL)
/
INSERT INTO NEGOTIATION_STATUS (NEGOTIATION_STATUS_ID, NEGOTIATION_STATUS_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID, ACTV_IND, VER_NBR)
VALUES ((SELECT (MAX(ID)) FROM NEGOTIATION_STATUS_S), 'COM', 'Complete', NOW(), 'admin', UUID(), 'Y', 1)
/
INSERT INTO NEGOTIATION_STATUS_S VALUES(NULL)
/
INSERT INTO NEGOTIATION_STATUS (NEGOTIATION_STATUS_ID, NEGOTIATION_STATUS_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID, ACTV_IND, VER_NBR)
VALUES ((SELECT (MAX(ID)) FROM NEGOTIATION_STATUS_S), 'SP', 'Suspended', NOW(), 'admin', UUID(), 'Y', 1)
/
INSERT INTO NEGOTIATION_AGREEMENT_TYPE_S VALUES(NULL)
/
-- insert agreement types
INSERT INTO NEGOTIATION_AGREEMENT_TYPE (NEGOTIATION_AGRMNT_TYPE_ID, NEGOTIATION_AGRMNT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID, ACTV_IND, VER_NBR)
VALUES ((SELECT (MAX(ID)) FROM NEGOTIATION_AGREEMENT_TYPE_S), 'SRA', 'Standard Research Agreement', NOW(), 'admin', UUID(), 'Y', 1)
/
INSERT INTO NEGOTIATION_AGREEMENT_TYPE_S VALUES(NULL)
/
INSERT INTO NEGOTIATION_AGREEMENT_TYPE (NEGOTIATION_AGRMNT_TYPE_ID, NEGOTIATION_AGRMNT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID, ACTV_IND, VER_NBR)
VALUES ((SELECT (MAX(ID)) FROM NEGOTIATION_AGREEMENT_TYPE_S), 'NDA', 'Non-Disclosure Agreement', NOW(), 'admin', UUID(), 'Y', 1)
/
INSERT INTO NEGOTIATION_AGREEMENT_TYPE_S VALUES(NULL)
/
INSERT INTO NEGOTIATION_AGREEMENT_TYPE (NEGOTIATION_AGRMNT_TYPE_ID, NEGOTIATION_AGRMNT_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID, ACTV_IND, VER_NBR)
VALUES ((SELECT (MAX(ID)) FROM NEGOTIATION_AGREEMENT_TYPE_S), 'MTA', 'Material Transfer Agreement', NOW(), 'admin', UUID(), 'Y', 1)
/
INSERT INTO NEGOTIATION_ASSOCIATION_TYPE_S VALUES(NULL)
/
-- insert association types
INSERT INTO NEGOTIATION_ASSOCIATION_TYPE (NEGOTIATION_ASSC_TYPE_ID, NEGOTIATION_ASSC_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID, ACTV_IND, VER_NBR)
VALUES ((SELECT (MAX(ID)) FROM NEGOTIATION_ASSOCIATION_TYPE_S), 'NO', 'None', NOW(), 'admin', UUID(), 'Y', 1)
/
INSERT INTO NEGOTIATION_ASSOCIATION_TYPE_S VALUES(NULL)
/
INSERT INTO NEGOTIATION_ASSOCIATION_TYPE (NEGOTIATION_ASSC_TYPE_ID, NEGOTIATION_ASSC_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID, ACTV_IND, VER_NBR)
VALUES ((SELECT (MAX(ID)) FROM NEGOTIATION_ASSOCIATION_TYPE_S), 'PL', 'Proposal Log', NOW(), 'admin', UUID(), 'Y', 1)
/
INSERT INTO NEGOTIATION_ASSOCIATION_TYPE_S VALUES(NULL)
/
INSERT INTO NEGOTIATION_ASSOCIATION_TYPE (NEGOTIATION_ASSC_TYPE_ID, NEGOTIATION_ASSC_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID, ACTV_IND, VER_NBR)
VALUES ((SELECT (MAX(ID)) FROM NEGOTIATION_ASSOCIATION_TYPE_S), 'IP', 'Institutional Proposal', NOW(), 'admin', UUID(), 'Y', 1)
/
INSERT INTO NEGOTIATION_ASSOCIATION_TYPE_S VALUES(NULL)
/
INSERT INTO NEGOTIATION_ASSOCIATION_TYPE (NEGOTIATION_ASSC_TYPE_ID, NEGOTIATION_ASSC_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID, ACTV_IND, VER_NBR)
VALUES ((SELECT (MAX(ID)) FROM NEGOTIATION_ASSOCIATION_TYPE_S), 'AWD', 'Award', NOW(), 'admin', UUID(), 'Y', 1)
/
INSERT INTO NEGOTIATION_ASSOCIATION_TYPE_S VALUES(NULL)
/
INSERT INTO NEGOTIATION_ASSOCIATION_TYPE (NEGOTIATION_ASSC_TYPE_ID, NEGOTIATION_ASSC_TYPE_CODE, DESCRIPTION, UPDATE_TIMESTAMP, UPDATE_USER, OBJ_ID, ACTV_IND, VER_NBR)
VALUES ((SELECT (MAX(ID)) FROM NEGOTIATION_ASSOCIATION_TYPE_S), 'SWD', 'Subaward', NOW(), 'admin', UUID(), 'Y', 1)
/
COMMIT
/
DELIMITER ;
