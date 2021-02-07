package ru.netology.moneytransferservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.moneytransferservice.dto.ConfirmOperationRequest;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.service.TransferMoneyService;
import ru.netology.moneytransferservice.util.ResponseUtil;

@Slf4j
@RestController
public class Controller {
    private final TransferMoneyService transferMoneyService;

    private final static String TRANSFER = "/transfer";
    private final static String CONFIRM_OPERATION = "/confirmOperation";

    public Controller(TransferMoneyService transferMoneyService) {
        this.transferMoneyService = transferMoneyService;
    }

    @PostMapping(TRANSFER)
    public ResponseEntity<Response> createTransferMoneyOperation(@RequestBody TransferMoneyRequest transferMoneyRequest) {
        return transferMoneyService.createTransferMoneyOperation(transferMoneyRequest);
    }

    @PostMapping(CONFIRM_OPERATION)
    public ResponseEntity<Response> confirmOperationRequest(@RequestBody ConfirmOperationRequest confirmOperationRequest) {
        return transferMoneyService.confirmTransferMoneyOperation(confirmOperationRequest);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handleRTE(RuntimeException e) {
        log.error(e.getMessage());
        Response response = ResponseUtil.getServerErrorResponse();
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
