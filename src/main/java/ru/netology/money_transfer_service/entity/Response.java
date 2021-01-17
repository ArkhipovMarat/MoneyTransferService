package ru.netology.money_transfer_service.entity;

import org.springframework.http.HttpStatus;

public class Response {
    private HttpStatus httpStatus;
    private String operationId;
    private String message;
    private int id;

    public Response() {
    }

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

    public String getOperationId() {
        return operationId;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
