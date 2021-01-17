package ru.netology.money_transfer_service.entity;

public class MoneyData {
    private String number;
    private String valid;
    private String cvv;
    private int balance;
    private String currency;

    public MoneyData() {}

    public MoneyData(String number, String valid, String cvv, int balance, String currency) {
        this.number = number;
        this.valid = valid;
        this.cvv = cvv;
        this.balance = balance;
        this.currency = currency;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
