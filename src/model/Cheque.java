package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Cheque {
    public enum ChequeStatus {
        ISSUED,
        CLEARED,
        BOUNCED,
        CANCELLED
    }

    private int id;
    private int accountId;
    private String chequeNumber;
    private LocalDate issueDate;
    private String payeeName;
    private double amount;
    private ChequeStatus status;
    private LocalDateTime clearedDate;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getChequeNumber() { return chequeNumber; }
    public void setChequeNumber(String chequeNumber) { this.chequeNumber = chequeNumber; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public String getPayeeName() { return payeeName; }
    public void setPayeeName(String payeeName) { this.payeeName = payeeName; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public ChequeStatus getStatus() { return status; }
    public void setStatus(ChequeStatus status) { this.status = status; }

    public LocalDateTime getClearedDate() { return clearedDate; }
    public void setClearedDate(LocalDateTime clearedDate) { this.clearedDate = clearedDate; }
}
