package ru.netology.moneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.netology.moneytransferservice.dto.Amount;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.entity.OperationData;
import ru.netology.moneytransferservice.entity.UserAccount;
import ru.netology.moneytransferservice.repository.UsersAccountRepository;
import ru.netology.moneytransferservice.service.UserAccountService;
import ru.netology.moneytransferservice.util.ResponseUtil;

@SpringBootTest
public class UserAccountServiceIntegrationTest {
    @Autowired
    UserAccountService userAccountService;

    @Autowired
    UsersAccountRepository usersAccountRepository;

    // TEST INPUT DATA
    private static final String DATA_CARD_FROM_NUMBER_1 = "1111111111111111";
    private static final String DATA_CARD_FROM_NUMBER_2 = "2222222222222222";
    private static final String DATA_CARD_TO_NUMBER = DATA_CARD_FROM_NUMBER_2;
    private static final String DATA_CARD_FROM_CVV = "111";
    private static final String DATA_CARD_FROM_VALID_TILL = "11/21";

    private static final String CURRENCY = "RUR";
    private static final int AMOUNT_VALUE = 100000;
    private static final int WRONG_AMOUNT_VALUE = 100000_0;
    private static final int NULL_AMOUNT_VALUE = 0;
    private static final String VERIFICATION_CODE = "0000";
    private static final String OPERATION_ID = "12345f-12345";
    private static final int NONE_ID = 0;

    // RESPONSES
    private static final String MESSAGE_SUCCESS_VALIDATION = "Success validation";
    private static final String MESSAGE_SUCCESS_TRANSFER = "Success transfer";
    private static final String MESSAGE_INSUFFICIENT_FUNDS = "Insufficient funds";

    // SUCCESS VALIDATION
    @Test
    void validateTransferMoneyRequest_WhenSuccessValidation() {
        insertUsers();

        Response responseResult = ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_VALIDATION, NONE_ID);

        TransferMoneyRequest transferMoneyRequest = getValidTransferMoneyRequest();

        Response responseTest = userAccountService.validateTransferMoneyRequest(transferMoneyRequest);

        Assertions.assertEquals(responseResult, responseTest);
    }

    // SUCCESS TRANSFER
    @Test
    void transferMoney_WhenSuccessTransfer() {
        insertUsers();

        OperationData operationData = new OperationData(OPERATION_ID, VERIFICATION_CODE, getValidTransferMoneyRequest());

        Response responseTest = userAccountService.transferMoney(operationData);

        Response responseResult = ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_TRANSFER, NONE_ID);

        Assertions.assertEquals(responseResult, responseTest);

        UserAccount userAccountFrom = usersAccountRepository.getUserAccount(DATA_CARD_FROM_NUMBER_1);
        UserAccount userAccountTo = usersAccountRepository.getUserAccount(DATA_CARD_FROM_NUMBER_2);

        Assertions.assertEquals(NULL_AMOUNT_VALUE, userAccountFrom.getCardBalance());
        Assertions.assertEquals(AMOUNT_VALUE + AMOUNT_VALUE, userAccountTo.getCardBalance());
    }

    // ERROR TRANSFER
    @Test
    void transferMoney_WhenInsufficientFunds() {
        insertUsers();

        OperationData operationData = new OperationData(OPERATION_ID, VERIFICATION_CODE, getNonValidTransferMoneyRequest());

        Response responseTest = userAccountService.transferMoney(operationData);

        Response responseResult = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_INSUFFICIENT_FUNDS, NONE_ID);

        Assertions.assertEquals(responseResult, responseTest);

        UserAccount userAccountFrom = usersAccountRepository.getUserAccount(DATA_CARD_FROM_NUMBER_1);
        UserAccount userAccountTo = usersAccountRepository.getUserAccount(DATA_CARD_FROM_NUMBER_2);

        Assertions.assertEquals(AMOUNT_VALUE, userAccountFrom.getCardBalance());
        Assertions.assertEquals(AMOUNT_VALUE, userAccountTo.getCardBalance());
    }

    // GET USER ACCOUNT
    @Test
    void getUserAccount() {
        insertUsers();

        UserAccount userAccountFromTest = userAccountService.getUserAccount(DATA_CARD_FROM_NUMBER_1);
        UserAccount userAccountToTest = userAccountService.getUserAccount(DATA_CARD_FROM_NUMBER_2);

        UserAccount userAccountFromResult = new UserAccount(DATA_CARD_FROM_NUMBER_1, DATA_CARD_FROM_VALID_TILL,
                DATA_CARD_FROM_CVV, AMOUNT_VALUE, CURRENCY);

        UserAccount userAccountToResult = new UserAccount(DATA_CARD_FROM_NUMBER_2, DATA_CARD_FROM_VALID_TILL,
                DATA_CARD_FROM_CVV, AMOUNT_VALUE, CURRENCY);

        Assertions.assertEquals(userAccountFromResult,userAccountFromTest);
        Assertions.assertEquals(userAccountToResult,userAccountToTest);
    }

    // SET CHANGED USER ACCOUNT
    @Test
    void setUserAccount_WhenSetChangedUserAccount() {
        insertUsers();

        UserAccount changedUserAccount = new UserAccount(DATA_CARD_FROM_NUMBER_1, DATA_CARD_FROM_VALID_TILL,
                DATA_CARD_FROM_CVV, NULL_AMOUNT_VALUE, CURRENCY);

        userAccountService.setUserAccount(changedUserAccount);

        UserAccount userAccountTest = userAccountService.getUserAccount(DATA_CARD_FROM_NUMBER_1);

        Assertions.assertEquals(NULL_AMOUNT_VALUE, userAccountTest.getCardBalance());
    }


    private void insertUsers() {
        usersAccountRepository.setUserAccount(new UserAccount(DATA_CARD_FROM_NUMBER_1,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, AMOUNT_VALUE, CURRENCY));

        usersAccountRepository.setUserAccount(new UserAccount(DATA_CARD_FROM_NUMBER_2,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, AMOUNT_VALUE, CURRENCY));
    }

    // HELP METHODS
    TransferMoneyRequest getValidTransferMoneyRequest() {
        return new TransferMoneyRequest(DATA_CARD_FROM_NUMBER_1,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));
    }

    TransferMoneyRequest getNonValidTransferMoneyRequest() {
        return new TransferMoneyRequest(DATA_CARD_FROM_NUMBER_1,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, WRONG_AMOUNT_VALUE));
    }
}
