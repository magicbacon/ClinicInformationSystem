--<ScriptOptions statementTerminator=";"/>

CREATE TABLE provider (
		npi INT8 NOT NULL,
		name VARCHAR(255),
		specialization INT4
	);

CREATE UNIQUE INDEX provider_pkey ON provider (npi ASC);

ALTER TABLE provider ADD CONSTRAINT provider_pkey PRIMARY KEY (npi);

