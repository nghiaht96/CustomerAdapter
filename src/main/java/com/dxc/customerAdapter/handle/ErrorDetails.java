package com.dxc.customerAdapter.handle;

import java.util.Date;

public class ErrorDetails {
    private Date timstamp;
    private String message;

    public Date getTimstamp() {
        return timstamp;
    }

    public void setTimstamp(Date timstamp) {
        this.timstamp = timstamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    private String detail;

    public ErrorDetails(Date timstamp, String message, String detail) {
        this.timstamp = timstamp;
        this.message = message;
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ErrorDetails{" +
                "timstamp=" + timstamp +
                ", message='" + message + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
