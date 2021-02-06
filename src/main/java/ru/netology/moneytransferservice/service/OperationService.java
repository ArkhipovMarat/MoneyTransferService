package ru.netology.moneytransferservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.entity.OperationData;
import ru.netology.moneytransferservice.dto.ConfirmOperationRequest;
import ru.netology.moneytransferservice.dto.Response;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;
import ru.netology.moneytransferservice.repository.OperationRepository;
import ru.netology.moneytransferservice.util.IdCodeUtil;
import ru.netology.moneytransferservice.util.LoggerUtil;
import ru.netology.moneytransferservice.util.ResponseUtil;

@Slf4j
@Service
public class OperationService {
    private final OperationRepository operationRepository;

    private static final String MESSAGE_ERROR_INPUT = "Error input data";
    private static final String MESSAGE_ERROR_OPERATION = "Not found operation data";
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
                setTransferMoneyRequest(transferMoneyRequest);

        operationRepository.addOperation(operationData);
        log.info(LoggerUtil.createOperationLog(operationData));
//        LOGGER.info();
        return operationData;
    }

    public Response confirmOperationRequest(ConfirmOperationRequest confirmOperationRequest) {
        String operationId = confirmOperationRequest.getOperationId();
        OperationData operationData = operationRepository.getOperation(operationId);

        if (operationData==null)
            return ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_OPERATION, NONE_ID);

        if (!(confirmOperationRequest.getCode().equals(operationData.getCode())))
            return ResponseUtil.getResponse(HttpStatus.BAD_REQUEST, MESSAGE_ERROR_INPUT, NONE_ID);

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
