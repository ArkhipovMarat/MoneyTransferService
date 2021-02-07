package ru.netology.moneytransferservice;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import ru.netology.moneytransferservice.dto.Amount;
import ru.netology.moneytransferservice.dto.ConfirmOperationRequest;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {
    @Autowired
    TestRestTemplate testRestTemplate;

    // APPLICATION TEST CONTAINER
    // default host win7 docker
    private static final String APP_HOST = "http://192.168.99.100:";
    private static final int APP_PORT = 5500;
    private final static String APP_IMAGE = "mts";
    public static GenericContainer<?> transferMoneyApp = new GenericContainer<>(APP_IMAGE).withExposedPorts(APP_PORT);

    // Controller EndPoints
    private final static String ENDPOINT_TRANSFER = "/transfer";
    private final static String ENDPOINT_CONFIRM_OPERATION = "/confirmOperation";

    // TEST INPUT DATA
    private static final String DATA_CARD_FROM_NUMBER = "1111111111111111";
    private static final String DATA_CARD_FROM_CVV = "111";
    private static final String DATA_CARD_FROM_VALID_TILL = "11/21";
    private static final String DATA_CARD_TO_NUMBER = "2222222222222222";
    private static final String CURRENCY = "RUR";
    private static final int AMOUNT_VALUE = 1000;
    private static final int WRONG_AMOUNT_VALUE = 100000_0;
    private static final String VERIFICATION_CODE = "0000";
    private static final int NONE_ID = 0;

    // RESPONSES
    private static final String MESSAGE_SUCCESS_VALIDATION = "Success validation";
    private static final String MESSAGE_ERROR_INPUT = "Error input data";
    private static final String MESSAGE_INSUFFICIENT_FUNDS = "Insufficient funds";
    private static final String MESSAGE_SUCCESS_TRANSFER = "Success transfer";


    @BeforeAll
    public static void setUp() {
        transferMoneyApp.start();
    }

    // SUCCESS TRANSFER MONEY REQUEST
    @Test
    void performSuccessTransferMoneyRequest() {
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest(DATA_CARD_FROM_NUMBER,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));

        ResponseEntity<Response> testEntityCreateOperation = testRestTemplate
                .postForEntity(APP_HOST + transferMoneyApp.getMappedPort(APP_PORT) + ENDPOINT_TRANSFER, transferMoneyRequest, Response.class);

        String operationId = testEntityCreateOperation.getBody().getOperationId();

        Assertions.assertEquals(HttpStatus.OK, testEntityCreateOperation.getStatusCode());
        Assertions.assertEquals(MESSAGE_SUCCESS_VALIDATION, testEntityCreateOperation.getBody().getMessage());
        Assertions.assertTrue(isUUID(operationId));

        ConfirmOperationRequest confirmOperationRequest = new ConfirmOperationRequest(operationId, VERIFICATION_CODE);

        ResponseEntity<Response> testEntityConfirmOperation = testRestTemplate
                .postForEntity(APP_HOST + transferMoneyApp.getMappedPort(APP_PORT) + ENDPOINT_CONFIRM_OPERATION, confirmOperationRequest, Response.class);

        Assertions.assertEquals(HttpStatus.OK, testEntityConfirmOperation.getStatusCode());
        Assertions.assertEquals(MESSAGE_SUCCESS_TRANSFER, testEntityConfirmOperation.getBody().getMessage());
    }

    // ERROR INPUT DATA: WRONG CARD TO NUMBER
    @Test
    void performWrongCardToNumberRequest() {
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest(DATA_CARD_FROM_NUMBER,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, "",
                new Amount(CURRENCY, AMOUNT_VALUE));

        ResponseEntity<Response> testEntity = testRestTemplate
                .postForEntity(APP_HOST + transferMoneyApp.getMappedPort(APP_PORT) + ENDPOINT_TRANSFER, transferMoneyRequest, Response.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, testEntity.getStatusCode());
        Assertions.assertEquals(MESSAGE_ERROR_INPUT, testEntity.getBody().getMessage());
        Assertions.assertEquals(NONE_ID, testEntity.getBody().getId());
        Assertions.assertNull(testEntity.getBody().getOperationId());
    }


    // ERROR INPUT DATA: WRONG CARD FROM NUMBER
    @Test
    void performWrongCardFromNumberRequest() {
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest("",
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));

        ResponseEntity<Response> testEntity = testRestTemplate
                .postForEntity(APP_HOST + transferMoneyApp.getMappedPort(APP_PORT) + ENDPOINT_TRANSFER, transferMoneyRequest, Response.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, testEntity.getStatusCode());
        Assertions.assertEquals(MESSAGE_ERROR_INPUT, testEntity.getBody().getMessage());
        Assertions.assertEquals(NONE_ID, testEntity.getBody().getId());
        Assertions.assertNull(testEntity.getBody().getOperationId());
    }

    // ERROR INPUT DATA: WRONG CARD FROM VALID TILL
    @Test
    void performWrongCardFromValidTillRequest() {
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest(DATA_CARD_FROM_NUMBER,
                "", DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));

        ResponseEntity<Response> testEntity = testRestTemplate
                .postForEntity(APP_HOST + transferMoneyApp.getMappedPort(APP_PORT) + ENDPOINT_TRANSFER, transferMoneyRequest, Response.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, testEntity.getStatusCode());
        Assertions.assertEquals(MESSAGE_ERROR_INPUT, testEntity.getBody().getMessage());
        Assertions.assertEquals(NONE_ID, testEntity.getBody().getId());
        Assertions.assertNull(testEntity.getBody().getOperationId());
    }

    // ERROR INPUT DATA: WRONG CARD FROM CVV
    @Test
    void performWrongCardFromCvvRequest() {
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest(DATA_CARD_FROM_NUMBER,
                DATA_CARD_FROM_VALID_TILL, "", DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, AMOUNT_VALUE));

        ResponseEntity<Response> testEntity = testRestTemplate
                .postForEntity(APP_HOST + transferMoneyApp.getMappedPort(APP_PORT) + ENDPOINT_TRANSFER, transferMoneyRequest, Response.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, testEntity.getStatusCode());
        Assertions.assertEquals(MESSAGE_ERROR_INPUT, testEntity.getBody().getMessage());
        Assertions.assertEquals(NONE_ID, testEntity.getBody().getId());
        Assertions.assertNull(testEntity.getBody().getOperationId());
    }

    // ERROR INPUT DATA: INSUFFICIENT FUNDS
    @Test
    void performInsufficientFundsRequest() {
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest(DATA_CARD_FROM_NUMBER,
                DATA_CARD_FROM_VALID_TILL, DATA_CARD_FROM_CVV, DATA_CARD_TO_NUMBER,
                new Amount(CURRENCY, WRONG_AMOUNT_VALUE));

        ResponseEntity<Response> testEntity = testRestTemplate
                .postForEntity(APP_HOST + transferMoneyApp.getMappedPort(APP_PORT) + ENDPOINT_TRANSFER, transferMoneyRequest, Response.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, testEntity.getStatusCode());
        Assertions.assertEquals(MESSAGE_INSUFFICIENT_FUNDS, testEntity.getBody().getMessage());
        Assertions.assertEquals(NONE_ID, testEntity.getBody().getId());
        Assertions.assertNull(testEntity.getBody().getOperationId());
    }

    // HELP METHOD
    private boolean isUUID(String string) {
        try {
            UUID.fromString(string);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}