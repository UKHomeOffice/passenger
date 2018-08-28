UPDATE participant SET gender = 'MALE' WHERE gender = 'M';
UPDATE participant SET gender = 'FEMALE' WHERE gender = 'F';
UPDATE participant SET gender = 'UNKNOWN' WHERE gender <> 'F' AND gender <> 'M';