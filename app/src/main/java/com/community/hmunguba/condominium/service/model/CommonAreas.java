package com.community.hmunguba.condominium.service.model;

public class CommonAreas {

    private boolean hasGourmetArea;
    private boolean hasPoolArea;
    private boolean hasBarbecueArea;
    private boolean hasMoviesArea;
    private boolean hasPartyRoomArea;
    private boolean hasSportsCourtArea;

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
}
