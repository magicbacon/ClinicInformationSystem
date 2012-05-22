--<ScriptOptions statementTerminator=";"/>

CREATE TABLE surgerytreatment (
		id INT8 NOT NULL,
		date DATE
	);

CREATE TABLE patient (
		id INT8 NOT NULL,
		birthdate DATE,
		name VARCHAR(255),
		patientid INT8
	);

CREATE TABLE provider (
		npi INT8 NOT NULL,
		name VARCHAR(255),
		specialization INT4
	);

CREATE TABLE radiologytreatment (
		id INT8 NOT NULL
	);

CREATE TABLE radiologytreatment_dates (
		radiologytreatment_id INT8,
		dates VARCHAR(255)
	);

CREATE TABLE treatment (
		id INT8 NOT NULL,
		ttype VARCHAR(31),
		diagnosis VARCHAR(255),
		patient_fk INT8,
		provider_fk INT8
	);

CREATE TABLE sequence (
		seq_name VARCHAR(50) NOT NULL,
		seq_count NUMERIC(38 , 0)
	);

CREATE TABLE drugtreatment (
		id INT8 NOT NULL,
		dosage FLOAT8,
		drug VARCHAR(255)
	);

CREATE UNIQUE INDEX surgerytreatment_pkey ON surgerytreatment (id ASC);

CREATE UNIQUE INDEX provider_pkey ON provider (npi ASC);

CREATE UNIQUE INDEX treatment_pkey ON treatment (id ASC);

CREATE UNIQUE INDEX patient_pkey ON patient (id ASC);

CREATE UNIQUE INDEX radiologytreatment_pkey ON radiologytreatment (id ASC);

CREATE UNIQUE INDEX drugtreatment_pkey ON drugtreatment (id ASC);

CREATE UNIQUE INDEX sequence_pkey ON sequence (seq_name ASC);

ALTER TABLE treatment ADD CONSTRAINT treatment_pkey PRIMARY KEY (id);

ALTER TABLE patient ADD CONSTRAINT patient_pkey PRIMARY KEY (id);

ALTER TABLE sequence ADD CONSTRAINT sequence_pkey PRIMARY KEY (seq_name);

ALTER TABLE surgerytreatment ADD CONSTRAINT surgerytreatment_pkey PRIMARY KEY (id);

ALTER TABLE drugtreatment ADD CONSTRAINT drugtreatment_pkey PRIMARY KEY (id);

ALTER TABLE provider ADD CONSTRAINT provider_pkey PRIMARY KEY (npi);

ALTER TABLE radiologytreatment ADD CONSTRAINT radiologytreatment_pkey PRIMARY KEY (id);

ALTER TABLE treatment ADD CONSTRAINT fk_treatment_patient_fk FOREIGN KEY (patient_fk)
	REFERENCES patient (id);

ALTER TABLE radiologytreatment_dates ADD CONSTRAINT fk_radiologytreatment_dates_radiologytreatment_id FOREIGN KEY (radiologytreatment_id)
	REFERENCES treatment (id);

ALTER TABLE radiologytreatment ADD CONSTRAINT fk_radiologytreatment_id FOREIGN KEY (id)
	REFERENCES treatment (id);

ALTER TABLE drugtreatment ADD CONSTRAINT fk_drugtreatment_id FOREIGN KEY (id)
	REFERENCES treatment (id);

ALTER TABLE treatment ADD CONSTRAINT fk_treatment_provider_fk FOREIGN KEY (provider_fk)
	REFERENCES provider (npi);

ALTER TABLE surgerytreatment ADD CONSTRAINT fk_surgerytreatment_id FOREIGN KEY (id)
	REFERENCES treatment (id);

