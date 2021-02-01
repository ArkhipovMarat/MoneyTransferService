package ru.netology.moneytransferservice.util;

import org.springframework.http.HttpStatus;
import ru.netology.moneytransferservice.dto.Response;

public class ResponseUtil {
    private static final int NONE_ID = 0;
    private static final String SERVER_ERROR = "Internal Server Error";

    public static Response getResponse(HttpStatus httpStatus, String message, int id) {
        return new Response().
                setHttpStatus(httpStatus).
                setMessage(message).
                setId(id);
    }

    public static Response getServerErrorResponse() {
        return new Response().
                setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR).
                setMessage(SERVER_ERROR).
                setId(NONE_ID);
    }
}
