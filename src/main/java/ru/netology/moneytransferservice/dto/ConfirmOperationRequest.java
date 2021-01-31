package ru.netology.moneytransferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ConfirmOperationRequest {
    private String operationId;
    private String code;


//    public void setOperationId(String operationId) {
//        this.operationId = operationId;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getOperationId() {
//        return operationId;
//    }
//
//    public String getCode() {
//        return code;
//    }
}
