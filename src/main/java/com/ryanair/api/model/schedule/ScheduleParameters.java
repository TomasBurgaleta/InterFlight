package com.ryanair.api.model.schedule;

import java.io.Serializable;
import java.util.Objects;

public class ScheduleParameters implements Serializable {

    private String departure;
    private String arrival;
    private int year;
    private int month;

    public ScheduleParameters(String departure, String arrival, int year, int month) {
        this.departure = departure;
        this.arrival = arrival;
        this.year = year;
        this.month = month;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleParameters that = (ScheduleParameters) o;
        return year == that.year &&
                month == that.month &&
                Objects.equals(departure, that.departure) &&
                Objects.equals(arrival, that.arrival);
    }

    @Override
    public int hashCode() {

        return Objects.hash(departure, arrival, year, month);
    }
}
