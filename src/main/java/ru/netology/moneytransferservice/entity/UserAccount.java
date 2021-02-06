package ru.netology.moneytransferservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAccount {
    private String cardNumber;
    private String cardValid;
    private String cardCVV;
    private volatile int cardBalance;
    private String cardCurrency;
}
