package ru.netology.moneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import ru.netology.moneytransferservice.dto.Amount;
import ru.netology.moneytransferservice.dto.ConfirmOperationRequest;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.entity.OperationData;
import ru.netology.moneytransferservice.repository.OperationRepository;
import ru.netology.moneytransferservice.service.OperationService;
import ru.netology.moneytransferservice.util.ResponseUtil;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.UUID;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class OperationServiceTest {
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
    private static final String OPERATION_REPOSITORY = "operationRepository";

    private static final String MESSAGE_ERROR_INPUT = "Error input data";
    private static final String MESSAGE_ERROR_OPERATION = "Not found operation data";
    private static final String MESSAGE_SUCCESS_CONFIRMATION = "Success confirmation";
    private static final int NONE_ID = 0;

    private TransferMoneyRequest transferMoneyRequest;
    private OperationData operationData;
    private ConfirmOperationRequest confirmOperationRequest;
    private Response successConfirmationResult;
    private Response operationNotFoundResult;
    private Response badRequestResult;

    @BeforeAll
    void init() {
        // INPUT DATA
        transferMoneyRequest = new TransferMoneyRequest(DATA_CARD_FROM_NUMBER,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));

        operationData = new OperationData(OPERATION_ID, VERIFICATION_CODE, transferMoneyRequest);

        confirmOperationRequest = new ConfirmOperationRequest(OPERATION_ID, VERIFICATION_CODE);

        // RESULTS
        successConfirmationResult = ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_CONFIRMATION, NONE_ID)
                .setOperationId(OPERATION_ID);
        operationNotFoundResult = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_OPERATION, NONE_ID);
        badRequestResult = ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);
    }

    @Test
    void createOperationTest() {
        OperationData operationServiceResult = operationService.createOperation(transferMoneyRequest);

        Assertions.assertTrue(isUUID(operationServiceResult.getOperationId()));
        Assertions.assertEquals(VERIFICATION_CODE, operationServiceResult.getCode());
        Assertions.assertEquals(transferMoneyRequest, operationServiceResult.getTransferMoneyRequest());
    }

    @Test
    void confirmOperationRequest() {
        OperationRepository operationRepository = mock(OperationRepository.class);
        ReflectionTestUtils.setField(operationService, OPERATION_REPOSITORY, operationRepository);

        // CASE: SUCCESS CONFIRMATION
        when(operationRepository.getOperation(OPERATION_ID)).thenReturn(operationData);
        Assertions.assertEquals(successConfirmationResult, getTestResultConfirmOperation());

        // CASE: OPERATION NOT FOUND
        when(operationRepository.getOperation(OPERATION_ID)).thenReturn(null);
        Assertions.assertEquals(operationNotFoundResult, getTestResultConfirmOperation());

        // CASE: WRONG VERIFICATION CODE
        when(operationRepository.getOperation(OPERATION_ID)).thenReturn(operationData.setCode(""));
        Assertions.assertEquals(badRequestResult, getTestResultConfirmOperation());
    }

    private boolean isUUID(String string) {
        try {
            UUID.fromString(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Response getTestResultConfirmOperation() {
       return operationService.confirmOperationRequest(confirmOperationRequest);
    }

}
