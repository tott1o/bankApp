package model;

import java.time.LocalDateTime;

public class Transfer {
    public enum TransferMode {
        NEFT,
        RTGS
    }

    private int id;
    private int senderAccountNumber;
    private int receiverAccountNumber;
    private String receiverBankName;
    private String ifscCode;
    private double amount;
    private TransferMode mode;
    private LocalDateTime date;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSenderAccountNumber() { return senderAccountNumber; }
    public void setSenderAccountNumber(int senderAccountNumber) { this.senderAccountNumber = senderAccountNumber; }

    public int getReceiverAccountNumber() { return receiverAccountNumber; }
    public void setReceiverAccountNumber(int receiverAccountNumber) { this.receiverAccountNumber = receiverAccountNumber; }

    public String getReceiverBankName() { return receiverBankName; }
    public void setReceiverBankName(String receiverBankName) { this.receiverBankName = receiverBankName; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public TransferMode getMode() { return mode; }
    public void setMode(TransferMode mode) { this.mode = mode; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
