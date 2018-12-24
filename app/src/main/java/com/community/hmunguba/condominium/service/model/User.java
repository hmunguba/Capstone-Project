package com.community.hmunguba.condominium.service.model;

public class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String condominiumName;
    private String profilePic;
    private int houseNumber;
    private String phone;
    private String email;

    public User(String userId, String firstName, String lastName, String condominiumName,
                String profilePic, int houseNumber, String phone, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.condominiumName = condominiumName;
        this.profilePic = profilePic;
        this.houseNumber = houseNumber;
        this.phone = phone;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCondominium() {
        return condominiumName;
    }

    public void setCondominium(String condominiumName) {
        this.condominiumName = condominiumName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
