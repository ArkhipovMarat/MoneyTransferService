package ru.netology.moneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.moneytransferservice.dto.Amount;
import ru.netology.moneytransferservice.dto.ConfirmOperationRequest;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.entity.OperationData;
import ru.netology.moneytransferservice.repository.OperationRepository;
import ru.netology.moneytransferservice.service.OperationService;

@SpringBootTest
public class OperationServiceIntegrationTest {
    @Autowired
    OperationService operationService;

    @Autowired
    OperationRepository operationRepository;

    // TEST INPUT DATA
    private static final String DATA_CARD_FROM_NUMBER = "1111111111111111";
    private static final String DATA_CARD_FROM_CVV = "111";
    private static final String DATA_CARD_FROM_VALID_TILL = "11/21";
    private static final String DATA_CARD_TO_NUMBER = "2222222222222222";
    private static final String CURRENCY = "RUR";
    private static final int AMOUNT_VALUE = 100000;
    private static final String VERIFICATION_CODE = "0000";
    private static final String OPERATION_ID = "12345f-12345";

    // ADD OPERATION SUCCESS
    @Test
    void createOperation_WhenOperationRepositoryAddOperation() {
        TransferMoneyRequest transferMoneyRequest = getTransferMoneyRequest();

        OperationData operation = operationService.createOperation(transferMoneyRequest);

        OperationData operationTest = operationRepository.getOperation(operation.getOperationId());

        Assertions.assertEquals(transferMoneyRequest, operationTest.getTransferMoneyRequest());
    }

    // GET OPERATION SUCCESS
    @Test
    void confirmOperationRequest_WhenOperationRepositoryGetOperation() {
        OperationData operationDataResult = new OperationData(OPERATION_ID, VERIFICATION_CODE, getTransferMoneyRequest());

        operationRepository.addOperation(operationDataResult);

        ConfirmOperationRequest confirmOperationRequest = new ConfirmOperationRequest(OPERATION_ID, VERIFICATION_CODE);

        operationService.confirmOperationRequest(confirmOperationRequest);

        OperationData operationDataTest = operationRepository.getOperation(confirmOperationRequest.getOperationId());

        Assertions.assertEquals(operationDataResult, operationDataTest);
    }

    // REMOVE OPERATION SUCCESS
    @Test
    void removeOperation_WhenOperationRepositoryRemoveOperation() {
        OperationData operationData = new OperationData(OPERATION_ID, VERIFICATION_CODE, getTransferMoneyRequest());

        operationRepository.addOperation(operationData);

        operationService.removeOperation(operationData.getOperationId());

        Assertions.assertNull(operationRepository.getOperation(operationData.getOperationId()));
    }

    // FIND OPERATION SUCCESS
    @Test
    void getOperationData_WhenOperationRepositoryGetOperation() {
        OperationData operationDataResult = new OperationData(OPERATION_ID, VERIFICATION_CODE, getTransferMoneyRequest());

        operationRepository.addOperation(operationDataResult);

        OperationData operationDataTest = operationService.getOperationData(operationDataResult.getOperationId());

        Assertions.assertEquals(operationDataResult,operationDataTest);
    }

    // HELP METHODS
    TransferMoneyRequest getTransferMoneyRequest() {
        return new TransferMoneyRequest(DATA_CARD_FROM_NUMBER,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));
    }
}
