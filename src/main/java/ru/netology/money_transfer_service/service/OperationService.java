package ru.netology.money_transfer_service.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.netology.money_transfer_service.entity.OperationData;
import ru.netology.money_transfer_service.entity.ConfirmOperationRequest;
import ru.netology.money_transfer_service.entity.Response;
import ru.netology.money_transfer_service.entity.TransferMoneyRequest;
import ru.netology.money_transfer_service.repository.OperationRepository;
import ru.netology.money_transfer_service.util.IdCodeUtil;
import ru.netology.money_transfer_service.util.ResponseUtil;

@Service
public class OperationService {
    private final OperationRepository operationRepository;

    private static final String MESSAGE_ERROR_INPUT = "Error input data";
    private static final String MESSAGE_SUCCESS_CONFIRMATION = "Success confirmation";
    private static final int NONE_ID = 0;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public OperationData createOperation(TransferMoneyRequest transferMoneyRequest) {
        String operationId = IdCodeUtil.getOperationId();
        String verificationCode = IdCodeUtil.getVerificationCode();

        OperationData operationData = new OperationData();
        operationData.setOperationId(operationId).
                setCode(verificationCode).
                setTransferMoneyData(transferMoneyRequest).
                setStatus(true);

        operationRepository.addOperation(operationData);
        return operationData;
    }

    public Response confirmOperationRequest(ConfirmOperationRequest confirmOperationRequest) {
        String operationId = confirmOperationRequest.getOperationId();
        OperationData operationData = operationRepository.getOperation(operationId);

        if (!(confirmOperationRequest.getCode().equals(operationData.getCode()))) {
            return ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);
        }

        return ResponseUtil.getResponse(HttpStatus.OK, MESSAGE_SUCCESS_CONFIRMATION, NONE_ID).
                setOperationId(operationId);
    }

    public void removeOperation(String operationId) {
        operationRepository.removeOperation(operationId);
    }

    public OperationData getOperationData(String operationId) {
        return operationRepository.getOperation(operationId);
    }
}
