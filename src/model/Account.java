package model;

import java.time.LocalDate;

public class Account {
    public enum AccountType {
        SAVINGS,
        CURRENT,
        FIXED_DEPOSIT,
        RECURRING_DEPOSIT
    }

    private int id;
    private int customerId;
    private AccountType accountType;
    private LocalDate openDate;
    private LocalDate closeDate;
    private double balance;
    private double interestRate; // stored as percentage, e.g., 3.5 means 3.5%

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    public LocalDate getOpenDate() { return openDate; }
    public void setOpenDate(LocalDate openDate) { this.openDate = openDate; }

    public LocalDate getCloseDate() { return closeDate; }
    public void setCloseDate(LocalDate closeDate) { this.closeDate = closeDate; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
}
