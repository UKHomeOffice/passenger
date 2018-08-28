ALTER TABLE participant ADD emails_sent VARCHAR(1000);
UPDATE participant SET emails_sent = 'GRANTED' WHERE visa_email_sent = true;
