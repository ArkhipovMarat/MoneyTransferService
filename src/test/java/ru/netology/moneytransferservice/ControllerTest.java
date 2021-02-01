package ru.netology.moneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import ru.netology.moneytransferservice.dto.Amount;
import ru.netology.moneytransferservice.dto.ConfirmOperationRequest;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.util.ResponseUtil;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    private static final String DATA_CARD_FROM_NUMBER = "1111111111111111";
    private static final String DATA_CARD_FROM_CVV = "111";
    private static final String DATA_CARD_FROM_VALID_TILL = "11/21";
    private static final String DATA_CARD_TO_NUMBER = "2222222222222222";
    private static final String CURRENCY = "RUR";
    private static final int AMOUNT_VALUE = 1000;
    private static final String VERIFICATION_CODE = "0000";
    private static final String OPERATION_ID = "12345f-12345";
    private static final String OPERATION_REPOSITORY = "operationRepository";

    private static final String MESSAGE_SUCCESS_VALIDATION = "Success validation";
    private static final String MESSAGE_ERROR_INPUT = "Error input data";
    private static final String MESSAGE_ERROR_OPERATION = "Not found operation data";
    private static final String MESSAGE_SUCCESS_CONFIRMATION = "Success confirmation";
    private static final int NONE_ID = 0;


    private static final String CREATE_TRANSFER_MONEY_OPERATION_URL = "http://localhost:5500/transfer";

    @Test
    void validCreateTransferMoneyOperation() {
        TransferMoneyRequest validTransferMoneyRequest = getValidTransferMoneyRequest();

        Response result = getSuccessValidationResponse();
        Response testResult = getTestResult(CREATE_TRANSFER_MONEY_OPERATION_URL, validTransferMoneyRequest);

        Assertions.assertEquals(result.getHttpStatus(),testResult.getHttpStatus());
        Assertions.assertEquals(result.getMessage(),testResult.getMessage());
    }

    @Test
    void errorCreateTransferMoneyOperation() {
        TransferMoneyRequest errorCardDataTransferMoneyRequest = getErrorCardDataTransferMoneyRequest();

        Response result = getErrorValidationResponse();
        Response testResult = getTestResult(CREATE_TRANSFER_MONEY_OPERATION_URL, errorCardDataTransferMoneyRequest);

        Assertions.assertEquals(result,testResult);
    }


    public TransferMoneyRequest getValidTransferMoneyRequest() {
        return new TransferMoneyRequest(DATA_CARD_FROM_NUMBER,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));
    }

    public TransferMoneyRequest getErrorCardDataTransferMoneyRequest() {
        return new TransferMoneyRequest("",
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));
    }

    public Response getSuccessValidationResponse() {
        return ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_VALIDATION, NONE_ID);
    }

    public Response getErrorValidationResponse() {
        return ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);
    }

    public Response getTestResult(String url, Object object) {
        return restTemplate.postForObject(url, object, Response.class);
    }

    public ConfirmOperationRequest getValidConfirmOperationRequest() {
        return new ConfirmOperationRequest(OPERATION_ID, VERIFICATION_CODE);
    }
}
