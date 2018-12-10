package com.community.hmunguba.condominium.service.model;

public class Condominium {

    private String condId;
    private String name;
    private String profilePic;
    private String location;
    private int number;
    private String zipCode;
    private String state;
    private String city;
    private CommonAreas commonAreas;
    private String syndicName;
    private String conciergePhoneNumber;

    public Condominium(String condId, String name, String profilePic, String location, int number,
                       String zipCode, String state, String city, CommonAreas commonAreas) {
        this.condId = condId;
        this.name = name;
        this.profilePic = profilePic;
        this.location = location;
        this.number = number;
        this.zipCode = zipCode;
        this.state = state;
        this.city = city;
        this.commonAreas = commonAreas;
    }

    public String getCondId() {
        return condId;
    }

    public void setCondId(String condId) {
        this.condId = condId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public CommonAreas getCommonAreas() {
        return commonAreas;
    }

    public void setCommonAreas(CommonAreas commonAreas) {
        this.commonAreas = commonAreas;
    }

    public String getSyndicName() {
        return syndicName;
    }

    public void setSyndicName(String syndicName) {
        this.syndicName = syndicName;
    }

    public String getConciergePhoneNumber() {
        return conciergePhoneNumber;
    }

    public void setConciergePhoneNumber(String conciergePhoneNumber) {
        this.conciergePhoneNumber = conciergePhoneNumber;
    }
}
