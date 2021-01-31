package ru.netology.moneytransferservice.service;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.entity.OperationData;
import ru.netology.moneytransferservice.entity.UserAccount;
import ru.netology.moneytransferservice.util.ResponseUtil;

import ru.netology.moneytransferservice.repository.UsersAccountRepository;

import java.util.Scanner;


@Service
public class UserAccountService {
    private final UsersAccountRepository usersAccountRepository;

    private static final String MESSAGE_ERROR_INPUT = "Error input data";
    private static final String MESSAGE_INSUFFICIENT_FUNDS = "Insufficient funds";
    private static final String MESSAGE_SUCCESS_VALIDATION = "Success validation";
    private static final String MESSAGE_SUCCESS_TRANSFER = "Success transfer";
    private static final int NONE_ID = 0;

    public UserAccountService(UsersAccountRepository usersAccountRepository) {
        this.usersAccountRepository = usersAccountRepository;
    }

    public Response validateTransferMoneyRequest(TransferMoneyRequest transferMoneyRequest) {
        UserAccount userAccountFrom = getUserAccount(transferMoneyRequest.getCardFromNumber());
        UserAccount userAccountTo = getUserAccount(transferMoneyRequest.getCardToNumber());

        if (userAccountFrom == null || userAccountTo == null)
            return ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);

        if (!(transferMoneyRequest.getCardFromNumber().equals(userAccountFrom.getCardNumber()) &
                transferMoneyRequest.getCardFromValidTill().equals(userAccountFrom.getCardValid()) &
                transferMoneyRequest.getCardFromCVV().equals(userAccountFrom.getCardCVV())) &
                transferMoneyRequest.getCardToNumber().equals(userAccountTo.getCardNumber())) {
            return ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);
        }

        if (transferMoneyRequest.getAmount().getValue() > userAccountFrom.getCardBalance())
            return ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_INSUFFICIENT_FUNDS, NONE_ID);


        return ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_VALIDATION, NONE_ID);
    }

    public Response transferMoney(OperationData operationData) {
        String cardNumberFrom = operationData.getTransferMoneyRequest().getCardFromNumber();
        String cardNumberTo = operationData.getTransferMoneyRequest().getCardToNumber();
        int amount = operationData.getTransferMoneyRequest().getAmount().getValue();

        UserAccount userAccountFrom = getUserAccount(cardNumberFrom);
        UserAccount userAccountTo = getUserAccount(cardNumberTo);

        synchronized (userAccountFrom) {
            synchronized (userAccountTo) {
                if (userAccountFrom.getCardBalance() < amount)
                    return ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_INSUFFICIENT_FUNDS, NONE_ID);

                userAccountFrom.setCardBalance(userAccountFrom.getCardBalance() - amount);
                setUserAccount(userAccountFrom);

                userAccountTo.setCardBalance(userAccountTo.getCardBalance() + amount);
                setUserAccount(userAccountTo);
            }
        }
        return ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_TRANSFER, NONE_ID);
    }

    public UserAccount getUserAccount(String cardNumber) {
        return usersAccountRepository.getUserAccount(cardNumber);
    }

    public void setUserAccount(UserAccount userAccount) {
        usersAccountRepository.setUserAccount(userAccount);
    }


}
