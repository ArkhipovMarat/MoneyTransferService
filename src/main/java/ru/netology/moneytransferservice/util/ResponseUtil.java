package ru.netology.moneytransferservice.util;

import org.springframework.http.HttpStatus;
import ru.netology.moneytransferservice.dto.Response;

public class ResponseUtil {
    private static final int NONE_ID = 0;

    public static Response getResponse(HttpStatus httpStatus, String message, int id) {
        return new Response().
                setHttpStatus(httpStatus).
                setMessage(message).
                setId(id);
    }

    public static Response getServerErrorResponse(String message) {
        return new Response().
                setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR).
                setMessage(message).
                setId(NONE_ID);
    }
}
