
Table bank.customers {
  id int [pk, increment]
  full_name varchar
  address varchar
  contact_no varchar
  email varchar
  created_at datetime
}


// Account Table
Enum bank.account_type {
  savings
  current
  fixed_deposit
  recurring_deposit
}

Table bank.accounts {
  id int [pk, increment]
  customer_id int [ref: > bank.customers.id, not null]
  account_type bank.account_type
  open_date date
  close_date date
}

Enum bank.transaction_type {
  deposit
  withdrawal
}

Table bank.transactions {
  id int [pk, increment]
  account_id int [ref: > bank.accounts.id, not null]
  transaction_type bank.transaction_type
  amount decimal
  date datetime
  narration varchar
}

Enum bank.loan_type {
  personal_loan
  home_loan
  vehicle_loan
  gold_loan
}

Table bank.loans {
  id int [pk, increment]
  customer_id int [ref: > bank.customers.id, not null]
  loan_type bank.loan_type
  amount_sanctioned decimal
  balance decimal
  open_date date
  close_date date
}

Table bank.loan_payments {
  id int [pk, increment]
  loan_id int [ref: > bank.loans.id, not null]
  disbursement_amount decimal
  receipt_no varchar
  payment_date datetime
  remaining_balance decimal
}

