insert into KIM_NAMESPACES_T (ID,NAME,DESCRIPTION) values(1,'KIM','KIM Namespace');
insert into KIM_NAMESPACES_T (ID,NAME,DESCRIPTION) values(2,'KRA','KRA Namespace');

insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(1,'CREATE_PROPOSAL','Create Proposal Development Document', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(2,'MODIFY_PROPOSAL','Modify Proposal Development Document', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(3,'VIEW_PROPOSAL','View Proposal Development Document', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(4,'MODIFY_NARRATIVE','Create/Modify Proposal Narrative', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(5,'VIEW_NARRATIVE','View Proposal Narrative', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(6,'MODIFY_BUDGET','Create/Modify Proposal Budget', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(7,'VIEW_BUDGET','View Proposal Budget', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(8,'MAINTAIN_PROPOSAL_ACCESS','Assign Users to Proposal Roles', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(9,'ALTER_PROPOSAL_DATA','Change proposal master data once the proposal is locked', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(10,'PRINT_PROPOSAL','Print proposal on a sponsor specific path', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(11,'CERTIFY','Certify', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(12,'SUBMIT_TO_SPONSOR','Submit a Proposal to Grants.gov', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(13,'SUBMIT_PROPOSAL','Submit a Proposal for approval', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(14,'CREATE_PROTOCOL','Create Protocol Document', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(15,'MODIFY_PROTOCOL','Modify Protocol Document', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(16,'VIEW_PROTOCOL','View Protocol Document', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(17,'MAINTAIN_PROTOCOL_ACCESS','Assign Users to Protocol Roles', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(18,'ADD_PROTOCOL_NOTES','Add Protocol Notes', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(19,'CREATE_AMMENDMENT','Create a new ammendment for a protocol', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(20,'CREATE_RENEWAL','Create a new renewal for a protocol', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(21,'MAINTAIN_PROTOCOL_RELATED_PROJ','Maintain protocol links to awards and proposals', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(22,'EDIT_PROTOCOL_BILLABLE','Edit Billable field', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(23,'ADD_COMMITTE','Add new committees in a unit', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(24,'ADMINSTRATIVE_CORRECTION','Perform Administrative Corrections on Protocols', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(25,'GENERATE_AGENDA','Generate Agenda', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(26,'GENERATE_MINUTES','Generate Minutes', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(27,'GENERATE_SCHEDULE','Generate Schedule', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(28,'MAINTAIN_IRB_CORRESP_TEMPLATE','Maintain correspondence templates for committees in a unit', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(29,'MAINTAIN_MEMBERSHIPS','Maintain membership details in committees in a unit', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(30,'MAINTAIN_MINUTES','Add/modify/delete minute entries in any schedule for committees in a unit', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(31,'MAINTAIN_PROTOCOL_SUBMISSIONS','Modify Protocol Submission details', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(32,'MAINTAIN_PROTO_REVIEW_COMMENTS','Maintain Protocol Review Comments', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(33,'MODIFY_COMMITTEE','Modify existing committees in a unit', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(34,'MODIFY_SCHEDULE','Modify schedule details for committees in a unit', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(35,'PERFORM_IRB_ACTIONS_ON_PROTO','Perform any IRB action on a protocol submitted to a committee', 2);
insert into KIM_PERMISSIONS_T (ID,NAME,DESCRIPTION,NAMESPACE_ID) values(36,'VIEW_RESTRICTED_NOTES','View Restricted Notes in Protocols', 2);

insert into KIM_ROLE_TYPE_T (ROLE_TYPE_CODE, DESCRIPTION) values ('P', 'Proposal');
insert into KIM_ROLE_TYPE_T (ROLE_TYPE_CODE, DESCRIPTION) values ('O', 'OSP');
insert into KIM_ROLE_TYPE_T (ROLE_TYPE_CODE, DESCRIPTION) values ('D', 'Department');
insert into KIM_ROLE_TYPE_T (ROLE_TYPE_CODE, DESCRIPTION) values ('R', 'Protocol');

insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(1,'Proposal Creator','Proposal Creator', 'D', 'N');
insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(2,'Aggregator','Proposal Aggregator', 'P', 'N');
insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(3,'Narrative Writer','Proposal Narrative Writer', 'P', 'N');
insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(4,'Budget Creator','Proposal Budget Creator', 'P', 'N');
insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(5,'Viewer','Proposal Viewer', 'P', 'N');
insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(6,'unassigned','Unassigned - no permissions', 'P', 'N');
insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(7,'OSP Administrator','OSP Administrator', 'O', 'Y');
insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(8,'Proposal Submission','Proposal Submission', 'O', 'Y');
insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(9,'Protocol Creator','Protocol Creator', 'D', 'N');
insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(10,'Protocol Aggregator','Protocol Aggregator', 'R', 'N');
insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(11,'Protocol Viewer','Protocol Viewer', 'R', 'N');
insert into KIM_ROLES_T (ID,NAME,DESCRIPTION, ROLE_TYPE_CODE, DESCEND_FLAG) values(12,'IRB Administrator','IRB Administrator', 'D', 'Y');

insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(1,1);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(2,2);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(2,3);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(2,4);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(2,5);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(2,6);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(2,7);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(2,8);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(2,13);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(3,2);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(3,3);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(3,4);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(3,5);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(3,7);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(4,2);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(4,3);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(4,5);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(4,6);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(4,7);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(5,3);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(5,5);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(5,7);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(2,11);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(7,3);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(7,9);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(8,9);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(8,10);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(2,10);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(3,10);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(4,10);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(5,10);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(8,12);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(7,5);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(7,7);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(9,14);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(10,15);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(10,16);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(10,17);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(10,18);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(10,19);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(10,20);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(10,21);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(11,16);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,22);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,23);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,24);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,25);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,26);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,27);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,28);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,29);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,30);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,31);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,32);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,33);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,34);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,35);
insert into KIM_ROLES_PERMISSIONS_T (ROLE_ID,PERMISSION_ID) values(12,36);


insert into KIM_PERSONS_T (ID, USERNAME, PASSWORD) values (1, 'quickstart', 'fK69ATFsAydwQuteang+xMva+Tc=');
insert into KIM_PERSONS_T (ID, USERNAME, PASSWORD) values (2, 'ljoconno', 'fK69ATFsAydwQuteang+xMva+Tc=');
insert into KIM_PERSONS_T (ID, USERNAME, PASSWORD) values (3, 'bhutchin', 'fK69ATFsAydwQuteang+xMva+Tc=');
insert into KIM_PERSONS_T (ID, USERNAME, PASSWORD) values (4, 'aslusar', 'fK69ATFsAydwQuteang+xMva+Tc=');
insert into KIM_PERSONS_T (ID, USERNAME, PASSWORD) values (5, 'tdurkin', 'fK69ATFsAydwQuteang+xMva+Tc=');
insert into KIM_PERSONS_T (ID, USERNAME, PASSWORD) values (6, 'pcberg', 'fK69ATFsAydwQuteang+xMva+Tc=');
insert into KIM_PERSONS_T (ID, USERNAME, PASSWORD) values (7, 'jtester', 'fK69ATFsAydwQuteang+xMva+Tc=');
insert into KIM_PERSONS_T (ID, USERNAME, PASSWORD) values (8, 'KULUSER', 'fK69ATFsAydwQuteang+xMva+Tc=');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (1, '000000003', 1, '000001', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (2, '000000003', 1, 'BL-IIDC', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (3, '000000003', 1, 'IN-CARD', 'Y', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (4, '000000004', 1, '000001', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (5, '000000004', 1, 'BL-IIDC', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (6, '000000004', 1, 'IN-CARD', 'Y', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (7, '000000005', 1, '000001', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (8, '000000005', 1, 'BL-IIDC', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (9, '000000005', 1, 'IN-CARD', 'Y', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (10, '000000006', 1, '000001', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (11, '000000006', 1, 'BL-IIDC', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (12, '000000006', 1, 'IN-CARD', 'Y', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (13, '000000001', 1, '000001', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (14, '000000001', 1, 'BL-IIDC', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (15, '000000001', 1, 'IN-CARD', 'Y', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (16, '000000002', 1, '000001', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (17, '000000002', 1, 'BL-IIDC', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (18, '000000002', 1, 'IN-CARD', 'Y', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (19, '000000008', 1, '000001', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (20, '000000008', 1, 'BL-IIDC', 'N', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (21, '000000008', 1, 'IN-CARD', 'Y', 'Y');

insert into UNIT_ACL (ID, PERSON_ID, ROLE_ID, UNIT_NUMBER, SUBUNITS, ACTIVE_FLAG)
values (22, '000000006', 1, '000001', 'Y', 'Y');

