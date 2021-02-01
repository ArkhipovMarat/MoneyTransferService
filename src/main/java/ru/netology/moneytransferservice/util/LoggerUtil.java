package ru.netology.moneytransferservice.util;

import ru.netology.moneytransferservice.entity.OperationData;

public class LoggerUtil {
    private static final String STATUS_OPERATION_CREATED = "STATUS: OPERATION CREATED";
    private static final String STATUS_MONEY_TRANSFER_SUCCESS = "STATUS: MONEY TRANSFER SUCCESS";

    public static String createOperationLog(OperationData operationData) {
        return STATUS_OPERATION_CREATED + operationData.getLog();
    }

    public static String transferMoneyLog(OperationData operationData) {
        return STATUS_MONEY_TRANSFER_SUCCESS + operationData.getLog();
    }
}
