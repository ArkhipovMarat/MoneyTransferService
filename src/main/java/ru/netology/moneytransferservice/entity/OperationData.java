package ru.netology.moneytransferservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.netology.moneytransferservice.dto.TransferMoneyRequest;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OperationData {
    private String operationId;
    private String code;
    private TransferMoneyRequest transferMoneyRequest;

    public OperationData setOperationId(String operationId) {
        this.operationId = operationId;
        return this;
    }

    public OperationData setCode(String code) {
        this.code = code;
        return this;
    }

    public OperationData setTransferMoneyRequest(TransferMoneyRequest transferMoneyRequest) {
        this.transferMoneyRequest = transferMoneyRequest;
        return this;
    }
}
