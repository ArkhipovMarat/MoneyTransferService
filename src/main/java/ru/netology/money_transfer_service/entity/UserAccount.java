package ru.netology.money_transfer_service.entity;

public class UserAccount {
    private String cardNumber;
    private String cardValid;
    private String cardCVV;
    private int cardBalance;
    private String cardCurrency;

    public UserAccount(String cardNumber, String cardValid, String cardCVV, int cardBalance, String cardCurrency) {
        this.cardNumber = cardNumber;
        this.cardValid = cardValid;
        this.cardCVV = cardCVV;
        this.cardBalance = cardBalance;
        this.cardCurrency = cardCurrency;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardValid() {
        return cardValid;
    }

    public void setCardValid(String cardValid) {
        this.cardValid = cardValid;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    public int getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(int cardBalance) {
        this.cardBalance = cardBalance;
    }

    public String getCardCurrency() {
        return cardCurrency;
    }

    public void setCardCurrency(String cardCurrency) {
        this.cardCurrency = cardCurrency;
    }
}
