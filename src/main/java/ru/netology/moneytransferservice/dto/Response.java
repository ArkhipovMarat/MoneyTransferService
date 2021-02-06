package ru.netology.moneytransferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Response {
    private HttpStatus httpStatus;
    private String operationId;
    private String message;
    private int id;

    public Response setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public Response setOperationId(String operationId) {
        this.operationId = operationId;
        return this;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public Response setId(int id) {
        this.id = id;
        return this;
    }
}
