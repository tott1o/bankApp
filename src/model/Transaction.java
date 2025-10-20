package model;

import java.time.LocalDateTime;

public class Transaction {
    public void setTransactionDate(LocalDateTime now) {
    }

    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL
    }

    private int id;
    private int accountId;
    private TransactionType transactionType;
    private double amount;
    private LocalDateTime date;
    private String narration;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getNarration() { return narration; }
    public void setNarration(String narration) { this.narration = narration; }
}
