package com.ryanair.api.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(departure, section.departure) &&
                Objects.equals(arrival, section.arrival);
    }

    @Override
    public int hashCode() {

        return Objects.hash(departure, arrival);
    }
}
