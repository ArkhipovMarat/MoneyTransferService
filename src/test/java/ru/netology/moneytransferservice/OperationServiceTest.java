package ru.netology.moneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import ru.netology.moneytransferservice.dto.Amount;
import ru.netology.moneytransferservice.dto.ConfirmOperationRequest;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.entity.OperationData;
import ru.netology.moneytransferservice.repository.OperationRepository;
import ru.netology.moneytransferservice.service.OperationService;
import ru.netology.moneytransferservice.util.ResponseUtil;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
public class OperationServiceTest {
    @MockBean
    OperationRepository operationRepository;

    @Autowired
    private OperationService operationService;

    private static final String DATA_CARD_FROM_NUMBER = "1111111111111111";
    private static final String DATA_CARD_FROM_CVV = "111";
    private static final String DATA_CARD_FROM_VALID_TILL = "11/21";
    private static final String DATA_CARD_TO_NUMBER = "2222222222222222";
    private static final String CURRENCY = "RUR";
    private static final int AMOUNT_VALUE = 1000;
    private static final String VERIFICATION_CODE = "0000";
    private static final String OPERATION_ID = "12345f-12345";
    private static final String MESSAGE_ERROR_INPUT = "Error input data";
    private static final String MESSAGE_ERROR_OPERATION = "Not found operation data";
    private static final String MESSAGE_SUCCESS_CONFIRMATION = "Success confirmation";
    private static final int NONE_ID = 0;

    @Test
    void createOperation() {
        TransferMoneyRequest transferMoneyRequestResult = getTransferMoneyRequest();

        OperationData testResult = operationService.createOperation(transferMoneyRequestResult);

        Assertions.assertTrue(isUUID(testResult.getOperationId()));
        Assertions.assertEquals(VERIFICATION_CODE, testResult.getCode());
        Assertions.assertEquals(transferMoneyRequestResult, testResult.getTransferMoneyRequest());
    }

    // SUCCESS CONFIRMATION
    @Test
    void confirmOperationRequestSuccessConfirmation() {
        Response successConfirmationResult =
                ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_CONFIRMATION, NONE_ID)
                        .setOperationId(OPERATION_ID);

        OperationData operationData =
                new OperationData(OPERATION_ID, VERIFICATION_CODE, getTransferMoneyRequest());

        when(operationRepository.getOperation(OPERATION_ID)).thenReturn(operationData);

        Response testResult = operationService.confirmOperationRequest(getConfirmOperationRequest());

        Assertions.assertEquals(successConfirmationResult, testResult);
    }

    // OPERATION NOT FOUND
    @Test
    void confirmOperationRequestNotFound() {
        Response operationNotFoundResult =
                ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_OPERATION, NONE_ID);

        when(operationRepository.getOperation(OPERATION_ID)).thenReturn(null);

        Response testResult = operationService.confirmOperationRequest(getConfirmOperationRequest());

        Assertions.assertEquals(operationNotFoundResult, testResult);
    }

    // WRONG VERIFICATION CODE
    @Test
    void confirmOperationRequestWrongCode() {
        Response wrongVerificationCodeResult = ResponseUtil
                .getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);

        OperationData operationData =
                new OperationData(OPERATION_ID, VERIFICATION_CODE, getTransferMoneyRequest());

        when(operationRepository.getOperation(OPERATION_ID)).thenReturn(operationData.setCode(""));

        Response testResult = operationService.confirmOperationRequest(getConfirmOperationRequest());

        Assertions.assertEquals(wrongVerificationCodeResult, testResult);
    }

    private boolean isUUID(String string) {
        try {
            UUID.fromString(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
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
