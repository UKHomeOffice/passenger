ALTER TABLE visa_type ADD COLUMN description VARCHAR(200);
UPDATE visa_type set description = name;