package ru.netology.money_transfer_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.netology.money_transfer_service.entity.ConfirmOperationRequest;
import ru.netology.money_transfer_service.entity.Response;
import ru.netology.money_transfer_service.entity.TransferMoneyRequest;
import ru.netology.money_transfer_service.service.TransferMoneyService;
import ru.netology.money_transfer_service.util.ResponseUtil;

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
