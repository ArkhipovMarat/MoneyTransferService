package ru.netology.money_transfer_service.entity.requestBody.transferPostData;

public class Amount {
    private int value;
    private String currency;

    public Amount(int value, String currency) {
        this.value = value;
        this.currency = currency;
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
