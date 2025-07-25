-- Disables foreign key checks to allow truncation of tables in any order
SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE profile;
-- Add TRUNCATE statements for any other tables you have

-- Re-enables foreign key checks
SET REFERENTIAL_INTEGRITY TRUE;