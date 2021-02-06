package ru.netology.moneytransferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConfirmOperationRequest {
    private String operationId;
    private String code;
}
