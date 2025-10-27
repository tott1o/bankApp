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
    private double interestRate; // Annual interest rate (percentage)
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

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    /**
     * Calculate the monthly EMI (Equated Monthly Installment) for this loan.
     * Uses the standard EMI formula: EMI = P * r * (1 + r)^n / ((1 + r)^n - 1)
     * where P = Principal (amount sanctioned)
     *       r = Monthly interest rate (annual rate / 12 / 100)
     *       n = Total number of months
     * @param tenureMonths The loan tenure in months
     * @return Monthly EMI amount, or 0 if interest rate is 0
     */
    public double calculateMonthlyEMI(int tenureMonths) {
        if (interestRate == 0 || tenureMonths <= 0) return amountSanctioned / tenureMonths;
        
        double monthlyRate = interestRate / 12.0 / 100.0;
        double powerTerm = Math.pow(1 + monthlyRate, tenureMonths);
        
        return amountSanctioned * monthlyRate * powerTerm / (powerTerm - 1);
    }
}

