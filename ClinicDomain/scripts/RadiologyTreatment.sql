--<ScriptOptions statementTerminator=";"/>

CREATE TABLE radiologytreatment (
		id INT8 NOT NULL
	);

CREATE TABLE radiologytreatment_dates (
		radiologytreatment_id INT8,
		dates VARCHAR(255)
	);

CREATE UNIQUE INDEX radiologytreatment_pkey ON radiologytreatment (id ASC);

ALTER TABLE radiologytreatment ADD CONSTRAINT radiologytreatment_pkey PRIMARY KEY (id);

ALTER TABLE radiologytreatment ADD CONSTRAINT fk_radiologytreatment_id FOREIGN KEY (id)
	REFERENCES treatment (id);

ALTER TABLE radiologytreatment_dates ADD CONSTRAINT fk_radiologytreatment_dates_radiologytreatment_id FOREIGN KEY (radiologytreatment_id)
	REFERENCES treatment (id);

