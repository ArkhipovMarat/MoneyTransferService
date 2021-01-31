package ru.netology.moneytransferservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.moneytransferservice.dto.ConfirmOperationRequest;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.service.TransferMoneyService;
import ru.netology.moneytransferservice.util.ResponseUtil;

@RestController
public class Controller {
    private final TransferMoneyService transferMoneyService;

    public Controller(TransferMoneyService transferMoneyService) {
        this.transferMoneyService = transferMoneyService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Response> createTransferMoneyOperation(@RequestBody TransferMoneyRequest transferMoneyRequest) {
        return transferMoneyService.createTransferMoneyOperation(transferMoneyRequest);
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<Response> confirmOperationRequest(@RequestBody ConfirmOperationRequest confirmOperationRequest) {
        return transferMoneyService.confirmTransferMoneyOperation(confirmOperationRequest);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handleRTE(RuntimeException e) {
        Response response = ResponseUtil.getServerErrorResponse(e.getMessage());
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
