-- Add interest_rate column to loans table
ALTER TABLE loans
  ADD COLUMN interest_rate DECIMAL(5,2) DEFAULT 0.0;

-- Optional: Add an index on interest_rate if you plan to query by rate
-- CREATE INDEX idx_loans_interest_rate ON loans(interest_rate);