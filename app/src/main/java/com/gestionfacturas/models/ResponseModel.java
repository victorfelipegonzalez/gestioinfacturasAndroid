package com.gestionfacturas.models;

public class ResponseModel {
    private int success;
    private String message;
    private Object data;

    public ResponseModel() {
    }

    public ResponseModel(int success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
