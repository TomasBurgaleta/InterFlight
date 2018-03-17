package com.ryanair.api.model;

public class Section {

    private String departure;
    private String arrival;

    public Section(String departure, String arrival) {
        this.departure = departure;
        this.arrival = arrival;
    }

    public Section() {
    }

    public String getRoute() {
        return departure + "-" + arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }
}
