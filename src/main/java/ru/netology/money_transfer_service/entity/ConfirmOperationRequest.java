package ru.netology.money_transfer_service.entity;

public class ConfirmOperationRequest {
    private String operationId;
    private String code;

    public String getOperationId() {
        return operationId;
    }

    public String getCode() {
        return code;
    }
}
