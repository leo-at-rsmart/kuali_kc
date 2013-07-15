create index idx_qrtz_ft_trig_name on kc_qrtz_fired_triggers(TRIGGER_NAME);
create index idx_qrtz_ft_trig_group on kc_qrtz_fired_triggers(TRIGGER_GROUP);
create index idx_qrtz_ft_trig_nm_gp on kc_qrtz_fired_triggers(TRIGGER_NAME,TRIGGER_GROUP);
create index idx_qrtz_ft_trig_volatile on kc_qrtz_fired_triggers(IS_VOLATILE);
create index idx_qrtz_ft_trig_inst_name on kc_qrtz_fired_triggers(INSTANCE_NAME);
create index idx_qrtz_ft_job_name on kc_qrtz_fired_triggers(JOB_NAME);
create index idx_qrtz_ft_job_group on kc_qrtz_fired_triggers(JOB_GROUP);
create index idx_qrtz_ft_job_stateful on kc_qrtz_fired_triggers(IS_STATEFUL);
create index idx_qrtz_ft_job_req_recovery on kc_qrtz_fired_triggers(REQUESTS_RECOVERY);