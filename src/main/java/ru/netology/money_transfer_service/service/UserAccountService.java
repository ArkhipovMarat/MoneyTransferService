package ru.netology.money_transfer_service.service;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.netology.money_transfer_service.entity.OperationData;
import ru.netology.money_transfer_service.entity.UserAccount;
import ru.netology.money_transfer_service.entity.Response;
import ru.netology.money_transfer_service.entity.TransferMoneyRequest;
import ru.netology.money_transfer_service.repository.UsersAccountRepository;
import ru.netology.money_transfer_service.util.ResponseUtil;


@Service
public class UserAccountService {
    private final UsersAccountRepository usersAccountRepository;

    private static final String MESSAGE_ERROR_INPUT = "Error input data";
    private static final String MESSAGE_INSUFFICIENT_FUNDS = "Insufficient funds";
    private static final String MESSAGE_SUCCESS_TRANSFER = "Success transfer";
    private static final int NONE_ID = 0;

    public UserAccountService(UsersAccountRepository usersAccountRepository) {
        this.usersAccountRepository = usersAccountRepository;
    }

    public Response validateTransferMoneyRequest(TransferMoneyRequest transferMoneyRequest) {
        UserAccount userAccountFrom = usersAccountRepository.getUserAccount(transferMoneyRequest.getCardFromNumber());
        UserAccount userAccountTo = usersAccountRepository.getUserAccount(transferMoneyRequest.getCardToNumber());

        if (userAccountFrom == null || userAccountTo == null)
            return ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);

        if (!(transferMoneyRequest.getCardFromNumber().equals(userAccountFrom.getCardNumber()) &
                transferMoneyRequest.getCardFromValidTill().equals(userAccountFrom.getCardValid()) &
                transferMoneyRequest.getCardFromCVV().equals(userAccountFrom.getCardCVV())) &
                transferMoneyRequest.getCardToNumber().equals(userAccountTo.getCardNumber())) {
            return ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);
        }

        if (transferMoneyRequest.getAmount().getValue() > userAccountFrom.getCardBalance()) {
            return ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_INSUFFICIENT_FUNDS, NONE_ID);
        }

        return ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_TRANSFER, NONE_ID);
    }

    public void transferMoney(OperationData operationData) {
        String cardNumberFrom = operationData.getTransferMoneyData().getCardFromNumber();
        String cardNumberTo = operationData.getTransferMoneyData().getCardToNumber();
        int amount = operationData.getTransferMoneyData().getAmount().getValue();

        UserAccount userAccountFrom = usersAccountRepository.getUserAccount(cardNumberFrom);
        userAccountFrom.setCardBalance(userAccountFrom.getCardBalance()-amount);
        usersAccountRepository.setUserAccount(userAccountFrom);

        UserAccount userAccountTo = usersAccountRepository.getUserAccount(cardNumberTo);
        userAccountTo.setCardBalance(userAccountTo.getCardBalance()+amount);
        usersAccountRepository.setUserAccount(userAccountTo);
    }
}
