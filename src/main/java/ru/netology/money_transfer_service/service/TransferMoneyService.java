package ru.netology.money_transfer_service.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.netology.money_transfer_service.entity.OperationData;
import ru.netology.money_transfer_service.entity.ConfirmOperationRequest;
import ru.netology.money_transfer_service.entity.Response;
import ru.netology.money_transfer_service.entity.TransferMoneyRequest;

@Service
public class TransferMoneyService {
    private final OperationService operationService;
    private final UserAccountService userAccountService;

    public TransferMoneyService(OperationService operationService, UserAccountService userAccountService) {
        this.operationService = operationService;
        this.userAccountService = userAccountService;
    }

    public ResponseEntity<Response> createTransferMoneyOperation(TransferMoneyRequest transferMoneyRequest) {

        Response response = userAccountService.validateTransferMoneyRequest(transferMoneyRequest);

        if (!HttpStatus.BAD_REQUEST.equals(response.getHttpStatus())) {
            OperationData operationData = operationService.createOperation(transferMoneyRequest);
            response.setOperationId(operationData.getOperationId());
        }
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }

    public ResponseEntity<Response> confirmTransferMoneyOperation(ConfirmOperationRequest confirmOperationRequest) {
        Response response = operationService.confirmOperationRequest(confirmOperationRequest);

        if (!HttpStatus.BAD_REQUEST.equals(response.getHttpStatus())) {
            String operationId = confirmOperationRequest.getOperationId();
            OperationData operationData = operationService.getOperationData(operationId);
            userAccountService.transferMoney(operationData);
            operationService.removeOperation(operationId);
        }
        return ResponseEntity.status(response.getHttpStatus()).body(response);
    }
}
