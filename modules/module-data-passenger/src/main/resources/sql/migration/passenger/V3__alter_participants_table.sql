ALTER TABLE participant ADD gwf_number VARCHAR(200) NULL;
ALTER TABLE participant ADD vaf_number VARCHAR(200) NULL;
ALTER TABLE participant ADD middle_name VARCHAR(200) NULL;
ALTER TABLE participant ADD nationality VARCHAR(200) NULL;
ALTER TABLE participant ADD flightNumber VARCHAR(200) NULL;
ALTER TABLE participant ADD departure_point VARCHAR(200) NULL;
ALTER TABLE participant ADD departure_time TIMESTAMP NULL;
ALTER TABLE participant ADD arrival_point VARCHAR(200) NULL;
ALTER TABLE participant ADD arrival_time TIMESTAMP NULL;

ALTER TABLE participant DROP spx_number;
ALTER TABLE participant DROP valid_from;
ALTER TABLE participant DROP condition_1;
ALTER TABLE participant DROP condition_2