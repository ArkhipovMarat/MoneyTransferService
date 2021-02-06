package ru.netology.moneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.netology.moneytransferservice.dto.Amount;
import ru.netology.moneytransferservice.dto.ConfirmOperationRequest;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.entity.OperationData;
import ru.netology.moneytransferservice.service.OperationService;
import ru.netology.moneytransferservice.service.TransferMoneyService;
import ru.netology.moneytransferservice.service.UserAccountService;
import ru.netology.moneytransferservice.util.ResponseUtil;

import static org.mockito.Mockito.*;

@SpringBootTest
public class TransferMoneyServiceTest {
    @Autowired
    TransferMoneyService transferMoneyService;

    @MockBean
    OperationService operationService;

    @MockBean
    UserAccountService userAccountService;

    private static final String DATA_CARD_FROM_NUMBER = "1111111111111111";
    private static final String DATA_CARD_FROM_CVV = "111";
    private static final String DATA_CARD_FROM_VALID_TILL = "11/21";
    private static final String DATA_CARD_TO_NUMBER = "2222222222222222";
    private static final String CURRENCY = "RUR";
    private static final int AMOUNT_VALUE = 1000;
    private static final String VERIFICATION_CODE = "0000";
    private static final String OPERATION_ID = "12345f-12345";
    private static final String MESSAGE_ERROR_INPUT = "Error input data";
    private static final String MESSAGE_SUCCESS_CONFIRMATION = "Success confirmation";
    private static final String MESSAGE_SUCCESS_TRANSFER = "Success transfer";
    private static final int NONE_ID = 0;

    // RESPONSE ENTITY: SUCCESS CONFIRMATION
    @Test
    void createTransferMoneyOperationSuccessConfirmation() {
        ResponseEntity<Response> successConfirmationEntityResult = ResponseEntity.status(HttpStatus.OK)
                .body(new Response(HttpStatus.OK, OPERATION_ID, MESSAGE_SUCCESS_CONFIRMATION, NONE_ID));

        Response successConfirmationResponse = new Response(HttpStatus.OK, "", MESSAGE_SUCCESS_CONFIRMATION, NONE_ID);
        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();
        OperationData operationData = new OperationData(OPERATION_ID, VERIFICATION_CODE, transferMoneyRequest);

        when(userAccountService.validateTransferMoneyRequest(transferMoneyRequest)).thenReturn(successConfirmationResponse);
        when(operationService.createOperation(transferMoneyRequest)).thenReturn(operationData);

        ResponseEntity<Response> testResult = transferMoneyService.createTransferMoneyOperation(getTransferMoneyRequest());

        InOrder inOrder = Mockito.inOrder(userAccountService, operationService);
        inOrder.verify(userAccountService).validateTransferMoneyRequest(transferMoneyRequest);
        inOrder.verify(operationService).createOperation(transferMoneyRequest);

        Assertions.assertEquals(successConfirmationEntityResult, testResult);
    }

    // RESPONSE ENTITY: BAD REQUEST
    @Test
    void createTransferMoneyOperationBadRequest() {
        Response badRequestResponse = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);

        ResponseEntity<Response> badRequestEntityResult = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(badRequestResponse);

        when(userAccountService.validateTransferMoneyRequest(any())).thenReturn(badRequestResponse);

        ResponseEntity<Response> testResult = transferMoneyService.createTransferMoneyOperation(getTransferMoneyRequest());

        Assertions.assertEquals(badRequestEntityResult, testResult);
    }

    // BAD REQUEST
    @Test
    void confirmTransferMoneyOperationBadRequest() {
        Response badRequestResponse = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);

        ResponseEntity<Response> badRequestEntityResult = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(badRequestResponse);

        when(operationService.confirmOperationRequest(any())).thenReturn(badRequestResponse);

        ResponseEntity<Response> testResult = transferMoneyService.confirmTransferMoneyOperation(getConfirmOperationRequest());

        Assertions.assertEquals(badRequestEntityResult, testResult);
    }

    // SUCCESS TRANSFER
    @Test
    void confirmTransferMoneyOperationSuccessTransfer() {
        Response successConfirmationResponse = ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_CONFIRMATION, NONE_ID)
                .setOperationId(OPERATION_ID);
        OperationData operationData = new OperationData(OPERATION_ID, VERIFICATION_CODE, getTransferMoneyRequest());
        Response successTransferResponse = ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_TRANSFER, NONE_ID);

        when(operationService.confirmOperationRequest(any())).thenReturn(successConfirmationResponse);
        when(operationService.getOperationData(OPERATION_ID)).thenReturn(operationData);
        when(userAccountService.transferMoney(operationData)).thenReturn(successTransferResponse);

        ResponseEntity<Response> successTransferEntityResult = ResponseEntity.status(HttpStatus.OK)
                .body(successTransferResponse);
        ResponseEntity<Response> testResult = transferMoneyService.confirmTransferMoneyOperation(getConfirmOperationRequest());

        InOrder inOrder = Mockito.inOrder(operationService,userAccountService);
        inOrder.verify(operationService).getOperationData(OPERATION_ID);
        inOrder.verify(userAccountService).transferMoney(operationData);
        inOrder.verify(operationService).removeOperation(OPERATION_ID);

        Assertions.assertEquals(successTransferEntityResult, testResult);
    }

    // HELP METHODS
    TransferMoneyRequest getTransferMoneyRequest() {
        return new TransferMoneyRequest(DATA_CARD_FROM_NUMBER,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));
    }

    ConfirmOperationRequest getConfirmOperationRequest() {
        return new ConfirmOperationRequest(OPERATION_ID, VERIFICATION_CODE);
    }
}
