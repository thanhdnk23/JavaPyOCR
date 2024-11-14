package com.web.app.ocrweb.payload;


public class ReturnObject<T> {
    private int status; // HTTP status code
    private String message; // Message describing the response
    private boolean success; // Flag indicating success or failure
    private T data; // Data when request is successful
    public ReturnObject(){
        
    }
    public ReturnObject(int status, String message, boolean success, T data) {
        this.status = status;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public ReturnObject(int status, String message, boolean success) {
        this.status = status;
        this.message = message;
        this.success = success;
    }

    // Getters and Setters

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}