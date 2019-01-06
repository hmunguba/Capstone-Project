package com.community.hmunguba.condominium.service.model;

import java.util.Date;

public class Event {

    private String eventId;
    private String createdBy;
    private String title;
    private Date date;
    private String simpleDate;
    private String year;
    private String month;
    private String day;
    private int numberOfParticipants;
    private CommonAreas reservedArea;
    private String startTime;
    private String endTime;
    private String condId;

    public Event() { }

    public Event(String eventId, String createdBy, String title, Date date, String simpleDate,
                 int numberOfParticipants, CommonAreas reservedArea, String startTime,
                 String endTime, String condId) {
        this.eventId = eventId;
        this.createdBy = createdBy;
        this.title = title;
        this.date = date;
        this.simpleDate = simpleDate;
        this.numberOfParticipants = numberOfParticipants;
        this.reservedArea = reservedArea;
        this.startTime = startTime;
        this.endTime = endTime;
        this.condId = condId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSimpleDate() {
        return simpleDate;
    }

    public void setSimpleDate(String simpleDate) {
        this.simpleDate = simpleDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
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

    public String getCondId() {
        return condId;
    }

    public boolean hasSameCommonAreas(Event eventB) {
        boolean hasMoviesArea = this.getReservedArea().isHasMoviesArea();
        boolean hasBarbecueArea = this.getReservedArea().isHasBarbecueArea();
        boolean hasGourmetArea = this.getReservedArea().isHasGourmetArea();
        boolean hasPoolArea = this.getReservedArea().isHasPoolArea();
        boolean hasPartyArea = this.getReservedArea().isHasPartyRoomArea();
        boolean hasSportsArea = this.getReservedArea().isHasSportsCourtArea();

        if (eventB.getReservedArea().isHasGourmetArea() == hasGourmetArea &&
                eventB.getReservedArea().isHasBarbecueArea() == hasBarbecueArea &&
                eventB.getReservedArea().isHasPoolArea() == hasPoolArea &&
                eventB.getReservedArea().isHasPartyRoomArea() == hasPartyArea &&
                eventB.getReservedArea().isHasMoviesArea() == hasMoviesArea &&
                eventB.getReservedArea().isHasSportsCourtArea() == hasSportsArea) {
            return true;
        }
        return false;
    }
}
