package ru.netology.moneytransferservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.moneytransferservice.dto.ConfirmOperationRequest;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.service.OperationService;
import ru.netology.moneytransferservice.service.TransferMoneyService;
import ru.netology.moneytransferservice.util.ResponseUtil;

@RestController
public class Controller {
    private final TransferMoneyService transferMoneyService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationService.class);

    private final static String ENDPOINT_TRANSFER = "/transfer";
    private final static String ENDPOINT_CONFIRM_OPERATION = "/confirmOperation";

    public Controller(TransferMoneyService transferMoneyService) {
        this.transferMoneyService = transferMoneyService;
    }

    @PostMapping(ENDPOINT_TRANSFER)
    public ResponseEntity<Response> createTransferMoneyOperation(@RequestBody TransferMoneyRequest transferMoneyRequest) {
        return transferMoneyService.createTransferMoneyOperation(transferMoneyRequest);
    }

    @PostMapping(ENDPOINT_CONFIRM_OPERATION)
    public ResponseEntity<Response> confirmOperationRequest(@RequestBody ConfirmOperationRequest confirmOperationRequest) {
        return transferMoneyService.confirmTransferMoneyOperation(confirmOperationRequest);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handleRTE(RuntimeException e) {
        LOGGER.error(e.getMessage());
        Response response = ResponseUtil.getServerErrorResponse();
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
