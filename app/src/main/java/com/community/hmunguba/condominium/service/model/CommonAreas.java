package com.community.hmunguba.condominium.service.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CommonAreas implements Parcelable {

    private boolean hasGourmetArea;
    private boolean hasPoolArea;
    private boolean hasBarbecueArea;
    private boolean hasMoviesArea;
    private boolean hasPartyRoomArea;
    private boolean hasSportsCourtArea;

    public CommonAreas() { }

    public CommonAreas(boolean hasGourmetArea, boolean hasPoolArea, boolean hasBarbecueArea,
                       boolean hasMoviesArea, boolean hasPartyRoomArea, boolean hasSportsCourtArea) {
        this.hasGourmetArea = hasGourmetArea;
        this.hasPoolArea = hasPoolArea;
        this.hasBarbecueArea = hasBarbecueArea;
        this.hasMoviesArea = hasMoviesArea;
        this.hasPartyRoomArea = hasPartyRoomArea;
        this.hasSportsCourtArea = hasSportsCourtArea;
    }

    public boolean isHasGourmetArea() {
        return hasGourmetArea;
    }

    public void setHasGourmetArea(boolean hasGourmetArea) {
        this.hasGourmetArea = hasGourmetArea;
    }

    public boolean isHasPoolArea() {
        return hasPoolArea;
    }

    public void setHasPoolArea(boolean hasPoolArea) {
        this.hasPoolArea = hasPoolArea;
    }

    public boolean isHasBarbecueArea() {
        return hasBarbecueArea;
    }

    public void setHasBarbecueArea(boolean hasBarbecueArea) {
        this.hasBarbecueArea = hasBarbecueArea;
    }

    public boolean isHasMoviesArea() {
        return hasMoviesArea;
    }

    public void setHasMoviesArea(boolean hasMoviesArea) {
        this.hasMoviesArea = hasMoviesArea;
    }

    public boolean isHasPartyRoomArea() {
        return hasPartyRoomArea;
    }

    public void setHasPartyRoomArea(boolean hasPartyRoomArea) {
        this.hasPartyRoomArea = hasPartyRoomArea;
    }

    public boolean isHasSportsCourtArea() {
        return hasSportsCourtArea;
    }

    public void setHasSportsCourtArea(boolean hasSportsCourtArea) {
        this.hasSportsCourtArea = hasSportsCourtArea;
    }

    public void setOnlyGourmetArea() {
        hasGourmetArea = true;
        hasPoolArea = false;
        hasBarbecueArea = false;
        hasMoviesArea = false;
        hasPartyRoomArea = false;
        hasSportsCourtArea = false;
    }

    public void setOnlyPoolArea() {
        hasGourmetArea = false;
        hasPoolArea = true;
        hasBarbecueArea = false;
        hasMoviesArea = false;
        hasPartyRoomArea = false;
        hasSportsCourtArea = false;
    }

    public void setOnlyBarbecueArea() {
        hasGourmetArea = false;
        hasPoolArea = false;
        hasBarbecueArea = true;
        hasMoviesArea = false;
        hasPartyRoomArea = false;
        hasSportsCourtArea = false;
    }

    public void setOnlyMoviesArea() {
        hasGourmetArea = false;
        hasPoolArea = false;
        hasBarbecueArea = false;
        hasMoviesArea = true;
        hasPartyRoomArea = false;
        hasSportsCourtArea = false;
    }

    public void setOnlyPartyRoomArea() {
        hasGourmetArea = false;
        hasPoolArea = false;
        hasBarbecueArea = false;
        hasMoviesArea = false;
        hasPartyRoomArea = true;
        hasSportsCourtArea = false;
    }

    public void setOnlySportsCourtArea() {
        hasGourmetArea = false;
        hasPoolArea = false;
        hasBarbecueArea = false;
        hasMoviesArea = false;
        hasPartyRoomArea = false;
        hasSportsCourtArea = true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (hasGourmetArea ? 1 : 0));
        parcel.writeByte((byte) (hasPoolArea ? 1 : 0));
        parcel.writeByte((byte) (hasBarbecueArea ? 1 : 0));
        parcel.writeByte((byte) (hasMoviesArea ? 1 : 0));
        parcel.writeByte((byte) (hasPartyRoomArea ? 1 : 0));
        parcel.writeByte((byte) (hasSportsCourtArea ? 1 : 0));
    }

    protected CommonAreas(Parcel in) {
        this.hasGourmetArea = in.readByte() != 0;
        this.hasPoolArea = in.readByte() != 0;
        this.hasBarbecueArea = in.readByte() != 0;
        this.hasMoviesArea = in.readByte() != 0;
        this.hasPartyRoomArea = in.readByte() != 0;
        this.hasSportsCourtArea = in.readByte() != 0;
    }

    public static final Parcelable.Creator<CommonAreas> CREATOR = new Parcelable.Creator<CommonAreas>() {
        @Override
        public CommonAreas createFromParcel(Parcel parcel) {
            return new CommonAreas(parcel);
        }

        @Override
        public CommonAreas[] newArray(int i) {
            return new CommonAreas[i];
        }
    };
}
