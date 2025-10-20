package model;

import java.time.LocalDate;

public class Loan {
    public enum LoanType {
        PERSONAL_LOAN,
        HOME_LOAN,
        VEHICLE_LOAN,
        GOLD_LOAN
    }

    private int id;
    private int customerId;
    private LoanType loanType;
    private double amountSanctioned;
    private double balance;
    private LocalDate openDate;
    private LocalDate closeDate;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public LoanType getLoanType() { return loanType; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }

    public double getAmountSanctioned() { return amountSanctioned; }
    public void setAmountSanctioned(double amountSanctioned) { this.amountSanctioned = amountSanctioned; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public LocalDate getOpenDate() { return openDate; }
    public void setOpenDate(LocalDate openDate) { this.openDate = openDate; }

    public LocalDate getCloseDate() { return closeDate; }
    public void setCloseDate(LocalDate closeDate) { this.closeDate = closeDate; }
}

