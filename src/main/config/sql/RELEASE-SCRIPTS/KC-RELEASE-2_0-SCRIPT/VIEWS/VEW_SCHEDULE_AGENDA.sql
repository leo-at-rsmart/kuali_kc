CREATE OR REPLACE VIEW OSP$SCHEDULE_AGENDA AS 
SELECT COMM_SCHEDULE.SCHEDULE_ID,
       SCHEDULE_AGENDA.AGENDA_NUMBER,
       SCHEDULE_AGENDA.AGENDA_NAME,
       SCHEDULE_AGENDA.PDF_STORE,
       SCHEDULE_AGENDA.CREATE_TIMESTAMP,
       SCHEDULE_AGENDA.CREATE_USER,
       SCHEDULE_AGENDA.UPDATE_TIMESTAMP,
       SCHEDULE_AGENDA.UPDATE_USER
FROM   SCHEDULE_AGENDA,
       COMM_SCHEDULE
WHERE  COMM_SCHEDULE.ID = SCHEDULE_AGENDA.SCHEDULE_ID_FK;