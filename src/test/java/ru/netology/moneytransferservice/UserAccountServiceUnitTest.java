package ru.netology.moneytransferservice;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import ru.netology.moneytransferservice.dto.Amount;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.entity.OperationData;
import ru.netology.moneytransferservice.entity.UserAccount;
import ru.netology.moneytransferservice.repository.UsersAccountRepository;
import ru.netology.moneytransferservice.service.UserAccountService;
import ru.netology.moneytransferservice.util.ResponseUtil;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserAccountServiceUnitTest {
    @Autowired
    UserAccountService userAccountService;

    @MockBean
    UsersAccountRepository usersAccountRepository;

    // TEST INPUT DATA
    private static final String DATA_CARD_FROM_NUMBER = "1111111111111111";
    private static final String DATA_CARD_FROM_CVV = "111";
    private static final String DATA_CARD_TO_CVV = "222";
    private static final String DATA_CARD_FROM_VALID_TILL = "11/21";
    private static final String DATA_CARD_TO_NUMBER = "2222222222222222";
    private static final String CURRENCY = "RUR";
    private static final int AMOUNT_VALUE = 100000;
    private static final int WRONG_AMOUNT_VALUE = 100000_0;
    private static final int NULL_AMOUNT_VALUE = 0;
    private static final String VERIFICATION_CODE = "0000";
    private static final String OPERATION_ID = "12345f-12345";
    private static final int NONE_ID = 0;

    // RESPONSES
    private static final String MESSAGE_ERROR_INPUT = "Error input data";
    private static final String MESSAGE_INSUFFICIENT_FUNDS = "Insufficient funds";
    private static final String MESSAGE_SUCCESS_VALIDATION = "Success validation";
    private static final String MESSAGE_SUCCESS_TRANSFER = "Success transfer";

    @BeforeEach
    public void mockUserAccountRepository() {
        when(usersAccountRepository.getUserAccount(DATA_CARD_TO_NUMBER)).thenReturn(getUserAccountTo());
        when(usersAccountRepository.getUserAccount(DATA_CARD_FROM_NUMBER)).thenReturn(getUserAccountFrom());
    }

    // SUCCESS VALIDATION
    @Test
    void validateTransferMoneyRequest_WhenSuccessValidation() {
        Response successValidationResult = ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_VALIDATION, NONE_ID);

        Response testResult = userAccountService.validateTransferMoneyRequest(getTransferMoneyRequest());

        Assertions.assertEquals(successValidationResult, testResult);
    }

    // ERROR INPUT DATA: WRONG CVV
    @Test
    void validateTransferMoneyRequest_WhenErrorCvv() {
        Response errorInputDataResult = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);

        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        transferMoneyRequest.setCardFromCVV("");

        Response testResult = userAccountService.validateTransferMoneyRequest(transferMoneyRequest);

        Assertions.assertEquals(errorInputDataResult, testResult);
    }

    // ERROR INPUT DATA: WRONG CARD FROM NUMBER
    @Test
    void validateTransferMoneyRequest_WhenErrorCardFromNumber() {
        Response errorInputDataResult = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);

        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        transferMoneyRequest.setCardFromNumber("");

        Response testResult = userAccountService.validateTransferMoneyRequest(transferMoneyRequest);

        Assertions.assertEquals(errorInputDataResult, testResult);
    }

    // ERROR INPUT DATA: WRONG CARD TO NUMBER
    @Test
    void validateTransferMoneyRequest_WhenErrorCardToNumber() {
        Response errorInputDataResult =
                ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);

        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        transferMoneyRequest.setCardToNumber("");

        Response testResult = userAccountService.validateTransferMoneyRequest(transferMoneyRequest);

        Assertions.assertEquals(errorInputDataResult, testResult);
    }

    // ERROR INPUT DATA: WRONG CARD FROM VALID TILL
    @Test
    void validateTransferMoneyRequest_WhenErrorCardFromValidTill() {
        Response errorInputDataResult = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);

        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        transferMoneyRequest.setCardFromValidTill("");

        Response testResult = userAccountService.validateTransferMoneyRequest(transferMoneyRequest);

        Assertions.assertEquals(errorInputDataResult, testResult);
    }

    // ERROR INPUT DATA: INSUFFICIENT FUNDS
    @Test
    void validateTransferMoneyRequest_WhenInsufficientFunds() {
        Response insufficientFundsResult = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_INSUFFICIENT_FUNDS, NONE_ID);

        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        transferMoneyRequest.setAmount(new Amount(CURRENCY, WRONG_AMOUNT_VALUE));

        Response testResult = userAccountService.validateTransferMoneyRequest(transferMoneyRequest);

        Assertions.assertEquals(insufficientFundsResult, testResult);
    }

    // SUCCESS TRANSFER
    @Test
    void transferMoney_WhenSuccessTransfer() {
        UserAccount userAccountTo = getUserAccountTo();
        UserAccount userAccountFrom = getUserAccountFrom();

        when(usersAccountRepository.getUserAccount(DATA_CARD_TO_NUMBER)).thenReturn(userAccountTo);
        when(usersAccountRepository.getUserAccount(DATA_CARD_FROM_NUMBER)).thenReturn(userAccountFrom);

        Response successTransferResult = ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_TRANSFER, NONE_ID);

        OperationData operationData = new OperationData(OPERATION_ID, VERIFICATION_CODE, getTransferMoneyRequest());

        Response testResult = userAccountService.transferMoney(operationData);

        Assertions.assertEquals(successTransferResult, testResult);
        Assertions.assertEquals(NULL_AMOUNT_VALUE, userAccountFrom.getCardBalance());
        Assertions.assertEquals((AMOUNT_VALUE + AMOUNT_VALUE), userAccountTo.getCardBalance());
    }

    // INSUFFICIENT FUNDS
    @Test
    void transferMoney_WhenInsufficientFunds() {
        Response insufficientFundsResult = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_INSUFFICIENT_FUNDS, NONE_ID);

        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        transferMoneyRequest.getAmount().setValue(WRONG_AMOUNT_VALUE);
        OperationData operationData = new OperationData(OPERATION_ID, VERIFICATION_CODE, transferMoneyRequest);

        Response testResult = userAccountService.transferMoney(operationData);

        Assertions.assertEquals(insufficientFundsResult, testResult);
    }

    // HELP METHODS
    TransferMoneyRequest getTransferMoneyRequest() {
        return new TransferMoneyRequest(DATA_CARD_FROM_NUMBER, DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));
    }

    UserAccount getUserAccountTo() {
        return new UserAccount(DATA_CARD_TO_NUMBER, DATA_CARD_FROM_VALID_TILL, DATA_CARD_TO_CVV, AMOUNT_VALUE, CURRENCY);
    }

    UserAccount getUserAccountFrom() {
        return new UserAccount(DATA_CARD_FROM_NUMBER, DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, AMOUNT_VALUE, CURRENCY);
    }
}
