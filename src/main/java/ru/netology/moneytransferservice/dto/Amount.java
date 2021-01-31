package ru.netology.moneytransferservice.dto;

public class Amount {
    private String currency;
    private int value;


    public Amount() {}

    public Amount(String currency, int value) {
        this.currency = currency;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
