package ru.netology.money_transfer_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.netology.money_transfer_service.entity.requestBody.confirmOperationPostData.OperationId;
import ru.netology.money_transfer_service.entity.requestBody.transferPostData.TransferPostData;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/transfer")
    public Map<String,String> getMoneyTransferRequest(@RequestBody TransferPostData transferPostData) {
        Map<String, String> map = new HashMap<>();
        map.put("operationId", "p1");
        return map;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/confirmOperation")
    public Map<String,String> confirmOperationRequest(@RequestBody OperationId operationId) {
        Map<String, String> map = new HashMap<>();
        map.put("operationId", "p1");
        return map;
    }
}
