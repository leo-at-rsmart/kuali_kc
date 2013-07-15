CREATE OR REPLACE PROCEDURE GET_S2S_OPPORTUNITY (
    AW_PROPOSAL_NUMBER IN  OSP$EPS_PROPOSAL.PROPOSAL_NUMBER%TYPE,
    CUR_GENERIC     IN OUT RESULT_SETS.CUR_GENERIC )

IS
BEGIN
  OPEN CUR_GENERIC FOR
    SELECT
		OPP.PROPOSAL_NUMBER,
		OPP.OPPORTUNITY_TITLE,
		OPP.COMPETETION_ID,
		OPP.OPENING_DATE,
		OPP.CLOSING_DATE,
		OPP.SCHEMA_URL,
		OPP.INSTRUCTION_URL,
		OPP.UPDATE_TIMESTAMP,
		OPP.UPDATE_USER,
		OPP.OPPORTUNITY_ID,
		OPP.CFDA_NUMBER,
		OPP.OPPORTUNITY,
		OPP.S2S_SUBMISSION_TYPE_CODE,
		OPP.REVISION_CODE,
		OPP.REVISION_OTHER_DESCRIPTION
    FROM
		OSP$S2S_OPPORTUNITY OPP
    WHERE
      OPP.PROPOSAL_NUMBER = AW_PROPOSAL_NUMBER;
END;
/

