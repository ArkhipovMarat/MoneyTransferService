package ru.netology.money_transfer_service.entity.requestBody.transferPostData;

public class TransferPostData {
    private String cardFromNumber;
    private String cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Amount amount;

    public TransferPostData(String cardFromNumber, String cardFromValidTill, String cardFromCVV, String cardToNumber, ru.netology.money_transfer_service.entity.requestBody.transferPostData.Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardFromValidTill = cardFromValidTill;
        this.cardFromCVV = cardFromCVV;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
    }

    public String getCardFromNumber() {
        return cardFromNumber;
    }

    public void setCardFromNumber(String cardFromNumber) {
        this.cardFromNumber = cardFromNumber;
    }

    public String getCardFromValidTill() {
        return cardFromValidTill;
    }

    public void setCardFromValidTill(String cardFromValidTill) {
        this.cardFromValidTill = cardFromValidTill;
    }

    public String getCardFromCVV() {
        return cardFromCVV;
    }

    public void setCardFromCVV(String cardFromCVV) {
        this.cardFromCVV = cardFromCVV;
    }

    public String getCardToNumber() {
        return cardToNumber;
    }

    public void setCardToNumber(String cardToNumber) {
        this.cardToNumber = cardToNumber;
    }

    public ru.netology.money_transfer_service.entity.requestBody.transferPostData.Amount getAmount() {
        return amount;
    }

    public void setAmount(ru.netology.money_transfer_service.entity.requestBody.transferPostData.Amount amount) {
        this.amount = amount;
    }
}
