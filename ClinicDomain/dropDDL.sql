ALTER TABLE TREATMENT DROP CONSTRAINT FK_TREATMENT_provider_fk
ALTER TABLE TREATMENT DROP CONSTRAINT FK_TREATMENT_patient_fk
ALTER TABLE DRUGTREATMENT DROP CONSTRAINT FK_DRUGTREATMENT_ID
ALTER TABLE RADIOLOGYTREATMENT DROP CONSTRAINT FK_RADIOLOGYTREATMENT_ID
ALTER TABLE SURGERYTREATMENT DROP CONSTRAINT FK_SURGERYTREATMENT_ID
ALTER TABLE RadiologyTreatment_DATES DROP CONSTRAINT FK_RadiologyTreatment_DATES_RadiologyTreatment_ID
DROP TABLE TREATMENT
DROP TABLE DRUGTREATMENT
DROP TABLE PATIENT
DROP TABLE RADIOLOGYTREATMENT
DROP TABLE SURGERYTREATMENT
DROP TABLE PROVIDER
DROP TABLE RadiologyTreatment_DATES
DELETE FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN'
