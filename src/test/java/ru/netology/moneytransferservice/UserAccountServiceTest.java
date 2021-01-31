package ru.netology.moneytransferservice;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import ru.netology.moneytransferservice.dto.Amount;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.entity.OperationData;
import ru.netology.moneytransferservice.entity.UserAccount;
import ru.netology.moneytransferservice.repository.UsersAccountRepository;
import ru.netology.moneytransferservice.service.UserAccountService;
import ru.netology.moneytransferservice.util.ResponseUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UserAccountServiceTest {
    @Autowired
    UserAccountService userAccountService;

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
    private static final String USERS_ACCOUNT_REPOSITORY = "usersAccountRepository";
    private static final int NONE_ID = 0;

    private static final String MESSAGE_ERROR_INPUT = "Error input data";
    private static final String MESSAGE_INSUFFICIENT_FUNDS = "Insufficient funds";
    private static final String MESSAGE_SUCCESS_VALIDATION = "Success validation";
    private static final String MESSAGE_SUCCESS_TRANSFER = "Success transfer";

    private Response successValidationResult;
    private Response errorInputDataResult;
    private Response insufficientFundsResult;
    private Response successTransferResult;

    @BeforeAll
    void init() {
        // RESULTS
        successValidationResult = ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_VALIDATION, NONE_ID);
        errorInputDataResult = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);
        insufficientFundsResult = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_INSUFFICIENT_FUNDS, NONE_ID);
        successTransferResult = ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_TRANSFER, NONE_ID);
    }

    @Test
    void validateTransferMoneyRequestTest() {
        // CASE: SUCCESS VALIDATION
        Assertions.assertEquals(successValidationResult, getTestResultSuccessValidation());

        // CASE: ERROR INPUT DATA/USER ACCOUNT NOT FOUND
        Assertions.assertEquals(errorInputDataResult, getTestResultWrongCVV());
        Assertions.assertEquals(errorInputDataResult, getTestResultWrongCardFromNumber());
        Assertions.assertEquals(errorInputDataResult, getTestResultWrongCardToNumber());
        Assertions.assertEquals(errorInputDataResult, getTestResultWrongValidTill());

        // CASE: INSUFFICIENT FUNDS
        Assertions.assertEquals(insufficientFundsResult, getTestResultInsufficientFunds());
    }

    @Test
    void transferMoneyTest() {
        UsersAccountRepository usersAccountRepository = mock(UsersAccountRepository.class);
        ReflectionTestUtils.setField(userAccountService, USERS_ACCOUNT_REPOSITORY, usersAccountRepository);

        // CASE: SUCCESS TRANSFER
        when(usersAccountRepository.getUserAccount(DATA_CARD_TO_NUMBER)).thenReturn(getUserAccountTo());
        when(usersAccountRepository.getUserAccount(DATA_CARD_FROM_NUMBER)).thenReturn(getUserAccountFrom());

        Assertions.assertEquals(successTransferResult, getTestResultSuccessTransfer());
        Assertions.assertEquals(NULL_AMOUNT_VALUE, getTestResultCardBalance(DATA_CARD_FROM_NUMBER));
        Assertions.assertEquals((AMOUNT_VALUE + AMOUNT_VALUE), getTestResultCardBalance(DATA_CARD_TO_NUMBER));

        // CASE: INSUFFICIENT FUNDS
        when(usersAccountRepository.getUserAccount(DATA_CARD_TO_NUMBER)).thenReturn(getUserAccountTo());
        when(usersAccountRepository.getUserAccount(DATA_CARD_FROM_NUMBER)).thenReturn(getUserAccountFrom());

        Assertions.assertEquals(insufficientFundsResult, getTestResultErrorTransfer());
    }

    Response getTestResultSuccessValidation() {
        return userAccountService.validateTransferMoneyRequest(getTransferMoneyRequest());
    }

    Response getTestResultWrongCVV() {
        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        transferMoneyRequest.setCardFromCVV("");
        return userAccountService.validateTransferMoneyRequest(transferMoneyRequest);
    }

    Response getTestResultWrongCardFromNumber() {
        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        transferMoneyRequest.setCardFromNumber("");
        return userAccountService.validateTransferMoneyRequest(transferMoneyRequest);
    }

    Response getTestResultWrongCardToNumber() {
        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        transferMoneyRequest.setCardToNumber("");
        return userAccountService.validateTransferMoneyRequest(transferMoneyRequest);
    }

    Response getTestResultWrongValidTill() {
        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        transferMoneyRequest.setCardFromValidTill("");
        return userAccountService.validateTransferMoneyRequest(transferMoneyRequest);
    }

    Response getTestResultInsufficientFunds() {
        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        transferMoneyRequest.setAmount(new Amount(CURRENCY, WRONG_AMOUNT_VALUE));
        return userAccountService.validateTransferMoneyRequest(transferMoneyRequest);
    }

    Response getTestResultSuccessTransfer() {
        return userAccountService.transferMoney(getOperationData());
    }

    Response getTestResultErrorTransfer() {
        OperationData operationData = getOperationData();
        operationData.getTransferMoneyRequest().getAmount().setValue(WRONG_AMOUNT_VALUE);
        return userAccountService.transferMoney(operationData);
    }

    TransferMoneyRequest getTransferMoneyRequest() {
        return new TransferMoneyRequest(DATA_CARD_FROM_NUMBER,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));
    }

    OperationData getOperationData() {
        return new OperationData(OPERATION_ID, VERIFICATION_CODE, getTransferMoneyRequest());
    }

    UserAccount getUserAccountTo() {
        return new UserAccount(DATA_CARD_TO_NUMBER,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_TO_CVV, AMOUNT_VALUE, CURRENCY);
    }

    UserAccount getUserAccountFrom() {
        return new UserAccount(DATA_CARD_FROM_NUMBER,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, AMOUNT_VALUE, CURRENCY);
    }

    int getTestResultCardBalance(String cardNumber) {
        return userAccountService.getUserAccount(cardNumber).getCardBalance();
    }
}
