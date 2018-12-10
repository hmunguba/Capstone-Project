package com.community.hmunguba.condominium.service.model;

import java.util.Date;

public class Event {

    private String eventId;
    private String title;
    private Date day;
    private int numberOfParticipants;
    private CommonAreas reservedArea;
    private String startTime;
    private String endTime;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public CommonAreas getReservedArea() {
        return reservedArea;
    }

    public void setReservedArea(CommonAreas reservedArea) {
        this.reservedArea = reservedArea;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
