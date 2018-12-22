package com.community.hmunguba.condominium.service.model;

public class AuthAnswer {

    private Boolean isSuccessful;
    private String message;

    public AuthAnswer() {

    }

    public AuthAnswer(Boolean isSucessful, String message) {
        this.isSuccessful = isSucessful;
        this.message = message;
    }

    public Boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        isSuccessful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
