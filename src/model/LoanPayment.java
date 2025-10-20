package model;

import java.time.LocalDateTime;

public class LoanPayment {
    private int id;
    private int loanId;
    private double disbursementAmount;
    private String receiptNo;
    private LocalDateTime paymentDate;
    private double remainingBalance;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public double getDisbursementAmount() { return disbursementAmount; }
    public void setDisbursementAmount(double disbursementAmount) { this.disbursementAmount = disbursementAmount; }

    public String getReceiptNo() { return receiptNo; }
    public void setReceiptNo(String receiptNo) { this.receiptNo = receiptNo; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public double getRemainingBalance() { return remainingBalance; }
    public void setRemainingBalance(double remainingBalance) { this.remainingBalance = remainingBalance; }
}
