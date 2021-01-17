package ru.netology.money_transfer_service.util;


import java.util.UUID;

public class IdCodeUtil {
    private static final String VERIFICATION_CODE = "0000";

    public static String getOperationId() {
        return UUID.randomUUID().toString();
    }

    public static String getVerificationCode() {
        // FRONT doesnt release verification_code
        // emulation
        return VERIFICATION_CODE;
    }
}
