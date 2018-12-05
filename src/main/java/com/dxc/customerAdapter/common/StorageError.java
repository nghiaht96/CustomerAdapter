package com.dxc.customerAdapter.common;

import org.springframework.http.HttpStatus;

public enum StorageError {
    UNEXPECTED(500, HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND(404,HttpStatus.NOT_FOUND),
    JSON_PARSE_FAIL(400,HttpStatus.BAD_REQUEST);
    private final int code;
    private final HttpStatus status;

    StorageError(int code, HttpStatus status) {
        this.code = code;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }
}
