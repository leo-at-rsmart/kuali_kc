ALTER TABLE COI_DISCL_PROJECTS 
ADD CONSTRAINT FK_COI_ISCLOSURE_PROJECTS 
FOREIGN KEY (COI_DISCLOSURE_ID) 
REFERENCES COI_DISCLOSURE (COI_DISCLOSURE_ID)
/
