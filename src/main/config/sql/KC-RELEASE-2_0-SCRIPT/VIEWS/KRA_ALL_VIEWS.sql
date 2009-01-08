CREATE OR REPLACE VIEW OSP$AFFILIATION_TYPE
AS
   SELECT
    AFFILIATION_TYPE_CODE,
    DESCRIPTION,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    AFFILIATION_TYPE 
/

CREATE OR REPLACE VIEW OSP$AWARD( "MIT_AWARD_NUMBER", "SEQUENCE_NUMBER", "MODIFICATION_NUMBER", "SPONSOR_AWARD_NUMBER", "STATUS_CODE", "TEMPLATE_CODE", "AWARD_EXECUTION_DATE", "AWARD_EFFECTIVE_DATE", "BEGIN_DATE", "SPONSOR_CODE", "ACCOUNT_NUMBER", "APPRVD_EQUIPMENT_INDICATOR", "APPRVD_FOREIGN_TRIP_INDICATOR", "APPRVD_SUBCONTRACT_INDICATOR", "PAYMENT_SCHEDULE_INDICATOR", "IDC_INDICATOR", "TRANSFER_SPONSOR_INDICATOR", "COST_SHARING_INDICATOR", "UPDATE_TIMESTAMP", "UPDATE_USER", "SPECIAL_REVIEW_INDICATOR", "SCIENCE_CODE_INDICATOR", "NSF_CODE" )
AS
   SELECT
    AWARD_NUMBER,
    SEQUENCE_NUMBER,
    MODIFICATION_NUMBER,
    SPONSOR_AWARD_NUMBER,
    STATUS_CODE,
    TEMPLATE_CODE,
    AWARD_EXECUTION_DATE,
    AWARD_EFFECTIVE_DATE,
    BEGIN_DATE,
    SPONSOR_CODE,
    ACCOUNT_NUMBER,
    APPRVD_EQUIPMENT_INDICATOR,
    APPRVD_FOREIGN_TRIP_INDICATOR,
    APPRVD_SUBCONTRACT_INDICATOR,
    PAYMENT_SCHEDULE_INDICATOR,
    IDC_INDICATOR,
    TRANSFER_SPONSOR_INDICATOR,
    COST_SHARING_INDICATOR,
    UPDATE_TIMESTAMP,
    UPDATE_USER,
    SPECIAL_REVIEW_INDICATOR,
    SCIENCE_CODE_INDICATOR,
    NSF_CODE
     FROM
    award 
/

CREATE OR REPLACE VIEW OSP$AWARD_APPROVED_SUBCONTRACT AS SELECT 
	AWARD_NUMBER MIT_AWARD_NUMBER, 
	SEQUENCE_NUMBER SEQUENCE_NUMBER,
	ORGANIZATION_NAME SUBCONTRACTOR_NAME,
	AMOUNT AMOUNT,
	UPDATE_TIMESTAMP UPDATE_TIMESTAMP, 
	UPDATE_USER UPDATE_USER
FROM AWARD_APPROVED_SUBAWARDS
/

CREATE OR REPLACE VIEW OSP$AWARD_COST_SHARING
AS
   SELECT
    AWARD_NUMBER MIT_AWARD_NUMBER,
    SEQUENCE_NUMBER SEQUENCE_NUMBER,
    FISCAL_YEAR FISCAL_YEAR,
    COST_SHARE_PERCENTAGE COST_SHARING_PERCENTAGE,
    COST_SHARE_TYPE_CODE COST_SHARING_TYPE_CODE,
    SOURCE SOURCE_ACCOUNT,
    DESTINATION DESTINATION_ACCOUNT,
    COMMITMENT_AMOUNT AMOUNT,
    UPDATE_TIMESTAMP UPDATE_TIMESTAMP,
    UPDATE_USER UPDATE_USER
     FROM
    AWARD_COST_SHARE 
/

CREATE OR REPLACE VIEW OSP$AWARD_COMMENTS
AS
   SELECT
    AWARD_NUMBER MIT_AWARD_NUMBER,
    SEQUENCE_NUMBER SEQUENCE_NUMBER,
    COMMENT_TYPE_CODE COMMENT_CODE,
    CHECKLIST_PRINT_FLAG CHECKLIST_PRINT_FLAG,
    COMMENTS COMMENTS,
    UPDATE_TIMESTAMP UPDATE_TIMESTAMP,
    UPDATE_USER UPDATE_USER
     FROM
    AWARD_COMMENT 
/

CREATE OR REPLACE VIEW OSP$AWARD_HEADER( "MIT_AWARD_NUMBER", "SEQUENCE_NUMBER", "PROPOSAL_NUMBER", "TITLE", "AWARD_TYPE_CODE", "SPECIAL_EB_RATE_OFF_CAMPUS", "SPECIAL_EB_RATE_ON_CAMPUS", "PRE_AWARD_AUTHORIZED_AMOUNT", "PRE_AWARD_EFFECTIVE_DATE", "CFDA_NUMBER", "DFAFS_NUMBER", "SUB_PLAN_FLAG", "PROCUREMENT_PRIORITY_CODE", "PRIME_SPONSOR_CODE", "NON_COMPETING_CONT_PRPSL_DUE", "COMPETING_RENEWAL_PRPSL_DUE", "BASIS_OF_PAYMENT_CODE", "METHOD_OF_PAYMENT_CODE", "PAYMENT_INVOICE_FREQ_CODE", "INVOICE_NUMBER_OF_COPIES", "FINAL_INVOICE_DUE", "ACTIVITY_TYPE_CODE", "ACCOUNT_TYPE_CODE", "UPDATE_TIMESTAMP", "UPDATE_USER" )
AS
   SELECT
    AWARD_NUMBER,
    SEQUENCE_NUMBER,
    PROPOSAL_NUMBER,
    TITLE,
    AWARD_TYPE_CODE,
    SPECIAL_EB_RATE_OFF_CAMPUS,
    SPECIAL_EB_RATE_ON_CAMPUS,
    PRE_AWARD_AUTHORIZED_AMOUNT,
    PRE_AWARD_EFFECTIVE_DATE,
    CFDA_NUMBER,
    DFAFS_NUMBER,
    SUB_PLAN_FLAG,
    PROCUREMENT_PRIORITY_CODE,
    PRIME_SPONSOR_CODE,
    NON_COMPETING_CONT_PRPSL_DUE,
    COMPETING_RENEWAL_PRPSL_DUE,
    BASIS_OF_PAYMENT_CODE,
    METHOD_OF_PAYMENT_CODE,
    PAYMENT_INVOICE_FREQ_CODE,
    INVOICE_NUMBER_OF_COPIES,
    FINAL_INVOICE_DUE,
    ACTIVITY_TYPE_CODE,
    ACCOUNT_TYPE_CODE,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    AWARD 
/

CREATE OR REPLACE VIEW OSP$AWARD_IDC_RATE
AS
   SELECT
    B.AWARD_NUMBER MIT_AWARD_NUMBER,
    B.SEQUENCE_NUMBER SEQUENCE_NUMBER,
    A.APPLICABLE_IDC_RATE APPLICABLE_IDC_RATE,
    A.IDC_RATE_TYPE_CODE IDC_RATE_TYPE_CODE,
    A.FISCAL_YEAR FISCAL_YEAR,
    A.ON_CAMPUS_FLAG ON_CAMPUS_FLAG,
    A.UNDERRECOVERY_OF_IDC UNDERRECOVERY_OF_IDC,
    A.SOURCE_ACCOUNT SOURCE_ACCOUNT,
    A.DESTINATION_ACCOUNT DESTINATION_ACCOUNT,
    A.START_DATE START_DATE,
    A.END_DATE END_DATE,
    A.UPDATE_TIMESTAMP UPDATE_TIMESTAMP,
    A.UPDATE_USER UPDATE_USER
     FROM
    AWARD_IDC_RATE A,
    AWARD B
    WHERE
    A.AWARD_ID = B.AWARD_ID 
/

CREATE OR REPLACE VIEW OSP$AWARD_REPORT_TERMS
AS
   SELECT
    AWARD_NUMBER MIT_AWARD_NUMBER,
    SEQUENCE_NUMBER,
    REPORT_CLASS_CODE,
    REPORT_CODE,
    FREQUENCY_CODE,
    FREQUENCY_BASE_CODE,
    OSP_DISTRIBUTION_CODE,
    CONTACT_TYPE_CODE,
    ROLODEX_ID,
    DUE_DATE,
    NUMBER_OF_COPIES,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    AWARD_REPORT_TERMS 
/

CREATE OR REPLACE VIEW OSP$AWARD_SCIENCE_CODE(MIT_AWARD_NUMBER, 
	SEQUENCE_NUMBER, 
	SCIENCE_CODE, 
	UPDATE_TIMESTAMP, 
	UPDATE_USER)
	AS SELECT 
	B.AWARD_NUMBER, 
	B.SEQUENCE_NUMBER, 
	A.SCIENCE_KEYWORD_CODE, 
	A.UPDATE_TIMESTAMP, 
	A.UPDATE_USER
FROM AWARD_SCIENCE_KEYWORD A,AWARD B
	where A.AWARD_ID=B.AWARD_ID
/

CREATE OR REPLACE VIEW OSP$COMMENT
AS
   SELECT
    COMMENT_TYPE_CODE COMMENT_CODE,
    DESCRIPTION DESCRIPTION,
    TEMPLATE_FLAG TEMPLATE_FLAG,
    CHECKLIST_FLAG CHECKLIST_FLAG,
    AWARD_COMMENT_SCREEN_FLAG AWARD_COMMENT_SCREEN_FLAG,
    UPDATE_TIMESTAMP UPDATE_TIMESTAMP,
    UPDATE_USER UPDATE_USER
     FROM
    COMMENT_TYPE 
/

CREATE OR REPLACE VIEW OSP$COMM_MEMBERSHIP_TYPE AS SELECT 
    MEMBERSHIP_TYPE_CODE, 
    DESCRIPTION, 
    UPDATE_TIMESTAMP, 
    UPDATE_USER
FROM COMM_MEMBERSHIP_TYPE
/

CREATE OR REPLACE VIEW OSP$CONTACT_TYPE
AS
   SELECT
    CONTACT_TYPE_CODE,
    DESCRIPTION,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    CONTACT_TYPE 
/

CREATE OR REPLACE VIEW OSP$COST_SHARING_TYPE
AS
   SELECT
    COST_SHARE_TYPE_CODE COST_SHARING_TYPE_CODE,
    DESCRIPTION DESCRIPTION,
    UPDATE_TIMESTAMP UPDATE_TIMESTAMP,
    UPDATE_USER UPDATE_USER
     FROM
    COST_SHARE_TYPE 
/

CREATE OR REPLACE VIEW OSP$DISTRIBUTION
AS
   SELECT
    OSP_DISTRIBUTION_CODE,
    DESCRIPTION,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    DISTRIBUTION 
/

CREATE OR REPLACE VIEW OSP$FREQUENCY
AS
   SELECT
    FREQUENCY_CODE,
    DESCRIPTION,
    NUMBER_OF_DAYS,
    NUMBER_OF_MONTHS,
    REPEAT_FLAG,
    PROPOSAL_DUE_FLAG,
    INVOICE_FLAG,
    UPDATE_TIMESTAMP,
    UPDATE_USER,
    ADVANCE_NUMBER_OF_DAYS,
    ADVANCE_NUMBER_OF_MONTHS
     FROM
    FREQUENCY 
/

CREATE OR REPLACE VIEW OSP$FREQUENCY_BASE
AS
   SELECT
    FREQUENCY_BASE_CODE,
    DESCRIPTION,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    FREQUENCY_BASE 
/

CREATE OR REPLACE VIEW OSP$IDC_RATE_TYPE
AS
   SELECT
    IDC_RATE_TYPE_CODE,
    DESCRIPTION,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    IDC_RATE_TYPE 
/

CREATE OR REPLACE VIEW OSP$PROTOCOL
AS
   SELECT
    PROTOCOL_NUMBER,
    SEQUENCE_NUMBER,
    PROTOCOL_TYPE_CODE,
    PROTOCOL_STATUS_CODE,
    TITLE,
    DESCRIPTION,
    APPLICATION_DATE,
    APPROVAL_DATE,
    EXPIRATION_DATE,
    LAST_APPROVAL_DATE,
    FDA_APPLICATION_NUMBER,
    REFERENCE_NUMBER_1,
    REFERENCE_NUMBER_2,
    IS_BILLABLE,
    SPECIAL_REVIEW_INDICATOR,
    VULNERABLE_SUBJECT_INDICATOR,
    KEY_STUDY_PERSON_INDICATOR,
    FUNDING_SOURCE_INDICATOR,
    CORRESPONDENT_INDICATOR,
    REFERENCE_INDICATOR,
    RELATED_PROJECTS_INDICATOR,
    CREATE_TIMESTAMP,
    CREATE_USER,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    PROTOCOL 
/

CREATE OR REPLACE VIEW OSP$PROTOCOL_INVESTIGATORS
AS
   SELECT
    PROTOCOL_NUMBER,
    SEQUENCE_NUMBER,
    PERSON_ID,
    PERSON_NAME,
    NON_EMPLOYEE_FLAG,
    PRINCIPAL_INVESTIGATOR_FLAG,
    AFFILIATION_TYPE_CODE,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    PROTOCOL_INVESTIGATORS 
/

CREATE OR REPLACE VIEW OSP$PROTOCOL_KEY_PERSONS
AS
   SELECT
    PROTOCOL_NUMBER,
    SEQUENCE_NUMBER,
    PERSON_ID,
    PERSON_NAME,
    PERSON_ROLE,
    NON_EMPLOYEE_FLAG,
    AFFILIATION_TYPE_CODE,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    PROTOCOL_KEY_PERSONS 
/

CREATE OR REPLACE VIEW OSP$PROTOCOL_LOCATION
AS
   SELECT
    PROTOCOL_NUMBER,
    SEQUENCE_NUMBER,
    PROTOCOL_ORG_TYPE_CODE,
    ORGANIZATION_ID,
    ROLODEX_ID,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    PROTOCOL_LOCATION 
/

CREATE OR REPLACE VIEW OSP$PROTOCOL_ORG_TYPE
AS
   SELECT
    PROTOCOL_ORG_TYPE_CODE,
    DESCRIPTION,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    PROTOCOL_ORG_TYPE 
/

CREATE OR REPLACE VIEW osp$protocol_reference_type
AS
   SELECT
    protocol_reference_type_code,
    description,
    update_timestamp,
    update_user
     FROM
    protocol_reference_type 
/

CREATE OR REPLACE VIEW osp$protocol_references
AS
   SELECT
    protocol_number,
    sequence_number,
    protocol_reference_number,
    protocol_reference_type_code,
    reference_key,
    application_date,
    approval_date,
    comments,
    update_timestamp,
    update_user
     FROM
    protocol_references 
/

CREATE OR REPLACE VIEW OSP$PROTOCOL_RESEARCH_AREAS
AS
   SELECT
    PROTOCOL_NUMBER,
    SEQUENCE_NUMBER,
    RESEARCH_AREA_CODE,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    PROTOCOL_RESEARCH_AREAS 
/

CREATE OR REPLACE VIEW OSP$PROTOCOL_TYPE
AS
   SELECT
    PROTOCOL_TYPE_CODE,
    DESCRIPTION,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    PROTOCOL_TYPE 
/

CREATE OR REPLACE VIEW OSP$PROTOCOL_UNITS
AS
   SELECT
    PROTOCOL_NUMBER,
    SEQUENCE_NUMBER,
    UNIT_NUMBER,
    LEAD_UNIT_FLAG,
    PERSON_ID,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    PROTOCOL_UNITS 
/

CREATE OR REPLACE VIEW OSP$PROTOCOL_VULNERABLE_SUB
AS
   SELECT
    PROTOCOL_NUMBER,
    SEQUENCE_NUMBER,
    VULNERABLE_SUBJECT_TYPE_CODE,
    SUBJECT_COUNT,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    PROTOCOL_VULNERABLE_SUB 
/

CREATE OR REPLACE VIEW OSP$REPORT_CLASS
AS
   SELECT
    REPORT_CLASS_CODE,
    DESCRIPTION,
    UPDATE_TIMESTAMP,
    UPDATE_USER,
    GENERATE_REPORT_REQUIREMENTS
     FROM
    REPORT_CLASS 
/

CREATE OR REPLACE VIEW OSP$REPORT
AS
   SELECT
    REPORT_CODE,
    DESCRIPTION,
    FINAL_REPORT_FLAG,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    REPORT 
/

CREATE OR REPLACE VIEW OSP$VALID_RATES
AS
   SELECT
    ON_CAMPUS_RATE,
    OFF_CAMPUS_RATE,
    RATE_CLASS_TYPE,
    ADJUSTMENT_KEY,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    VALID_RATES 
/

CREATE OR REPLACE VIEW OSP$VULNERABLE_SUBJECT_TYPE
AS
   SELECT
    VULNERABLE_SUBJECT_TYPE_CODE,
    DESCRIPTION,
    UPDATE_TIMESTAMP,
    UPDATE_USER
     FROM
    VULNERABLE_SUBJECT_TYPE 
/
