package ru.netology.money_transfer_service.entity;

public class OperationData {
    private String operationId;
    private String code;
    private boolean status;
    private TransferMoneyRequest transferMoneyData;

    public OperationData setOperationId(String operationId) {
        this.operationId = operationId;
        return this;
    }

    public OperationData setCode(String code) {
        this.code = code;
        return this;
    }

    public OperationData setStatus(boolean status) {
        return this;
    }

    public OperationData setTransferMoneyData(TransferMoneyRequest transferMoneyData) {
        this.transferMoneyData = transferMoneyData;
        return this;
    }

    public String getOperationId() {
        return operationId;
    }

    public String getCode() {
        return code;
    }

    public boolean isStatus() {
        return status;
    }

    public TransferMoneyRequest getTransferMoneyData() {
        return transferMoneyData;
    }
}
