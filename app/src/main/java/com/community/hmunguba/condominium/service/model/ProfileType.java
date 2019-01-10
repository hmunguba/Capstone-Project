package com.community.hmunguba.condominium.service.model;

public class ProfileType {

    private String email;
    private String type;

    public ProfileType() { }

    public ProfileType(String email, String type) {
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
