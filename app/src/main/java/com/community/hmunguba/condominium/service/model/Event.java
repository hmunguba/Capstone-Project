package com.community.hmunguba.condominium.service.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

    private String eventId;
    private String createdBy;
    private String title;
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

    public Event(String eventId, String createdBy, String title, String simpleDate,
                 int numberOfParticipants, CommonAreas reservedArea, String startTime,
                 String endTime, String condId) {
        this.eventId = eventId;
        this.createdBy = createdBy;
        this.title = title;
        this.simpleDate = simpleDate;
        this.numberOfParticipants = numberOfParticipants;
        this.reservedArea = reservedArea;
        this.startTime = startTime;
        this.endTime = endTime;
        this.condId = condId;
    }

    public Event(String eventId, String createdBy, String title, String simpleDate, String year,
                 String month, String day, int numberOfParticipants, CommonAreas reservedArea,
                 String startTime, String endTime, String condId) {
        this.eventId = eventId;
        this.createdBy = createdBy;
        this.title = title;
        this.simpleDate = simpleDate;
        this.year = year;
        this.month = month;
        this.day = day;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.eventId);
        parcel.writeString(this.createdBy);
        parcel.writeString(this.title);
        parcel.writeString(this.simpleDate);
        parcel.writeString(this.year);
        parcel.writeString(this.month);
        parcel.writeString(this.day);
        parcel.writeInt(this.numberOfParticipants);
        parcel.writeParcelable(this.reservedArea, i);
        parcel.writeString(this.startTime);
        parcel.writeString(this.endTime);
        parcel.writeString(this.condId);
    }

    protected Event(Parcel in) {
        this.eventId = in.readString();
        this.createdBy = in.readString();
        this.title = in.readString();
        this.simpleDate = in.readString();
        this.year = in.readString();
        this.month = in.readString();
        this.day = in.readString();
        this.numberOfParticipants = in.readInt();
        this.reservedArea = in.readParcelable(CommonAreas.class.getClassLoader());
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.condId = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel parcel) {
            return new Event(parcel);
        }

        @Override
        public Event[] newArray(int i) {
            return new Event[i];
        }
    };
}
