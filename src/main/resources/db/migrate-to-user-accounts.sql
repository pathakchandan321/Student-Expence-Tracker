-- Run this ONCE in MySQL Workbench only if you used the app before login was added.
-- First run: SELECT id, username FROM users;
-- Then replace 1 below with the id of the account that should own your old records.

USE student_budget_db;

SET @owner_user_id = 1;

-- Check that the selected account exists before moving old data.
SELECT id, username FROM users WHERE id = @owner_user_id;

-- Old rows were created before user accounts, so Hibernate gave them user_id = 0.
UPDATE expense SET user_id = @owner_user_id WHERE user_id IS NULL OR user_id = 0;
UPDATE monthly_budget SET user_id = @owner_user_id WHERE user_id IS NULL OR user_id = 0;
UPDATE recurring_expense SET user_id = @owner_user_id WHERE user_id IS NULL OR user_id = 0;

-- Remove the old unique rule: one budget month for the entire application.
-- The new rule is one budget month per user.
SET @old_index = (
    SELECT index_name FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'monthly_budget'
      AND column_name = 'month' AND non_unique = 0 AND index_name <> 'PRIMARY'
    LIMIT 1
);
SET @drop_sql = IF(@old_index IS NULL, 'SELECT 1',
    CONCAT('ALTER TABLE monthly_budget DROP INDEX `', @old_index, '`'));
PREPARE drop_statement FROM @drop_sql;
EXECUTE drop_statement;
DEALLOCATE PREPARE drop_statement;

ALTER TABLE monthly_budget ADD UNIQUE KEY unique_user_month (user_id, month);
ALTER TABLE expense ADD CONSTRAINT fk_expense_user FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE monthly_budget ADD CONSTRAINT fk_budget_user FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE recurring_expense ADD CONSTRAINT fk_recurring_user FOREIGN KEY (user_id) REFERENCES users(id);
