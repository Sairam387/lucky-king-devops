package com.sairam.game;

import java.sql.Timestamp;

public class Transaction {

    private int transactionId;
    private String playerName;
    private String transactionType;
    private int amount;
    private Timestamp transactionDate;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Transaction(
            int transactionId,
            String playerName,
            String transactionType,
            int amount,
            Timestamp transactionDate) {

        this.transactionId = transactionId;
        this.playerName = playerName;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public int getTransactionId() {
        return transactionId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getAmount() {
        return amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    // =========================================================
    // SETTERS
    // =========================================================

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "Transaction{" +
                "transactionId=" + transactionId +
                ", playerName='" + playerName + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                '}';
    }
}