package ru.netology.moneytransferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransferMoneyRequest {
    private String cardFromNumber;
    private String cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Amount amount;
}
