package com.ryanair.api.model.flightRoute;

import com.ryanair.api.model.schedule.Flight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FlightRoute {

    private LocalDate localDate;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private String departure;
    private String arrival;

    public FlightRoute(Flight flight, LocalDate localDate, String departure, String arrival) {
        this.localDate = localDate;
        this.departureDateTime = getLocalDateTime(localDate, flight.getDepartureTime());
        this.arrivalDateTime = getLocalDateTime(localDate, flight.getArrivalTime());
        this.departure = departure;
        this.arrival = arrival;
    }

    public String getRoute() {
        return departure + "-" + arrival;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
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

    public FlightRoute(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    private LocalDateTime getLocalDateTime(LocalDate localDate, String localTime) {
        LocalTime localtime = LocalTime.parse(localTime);
        return localDate.atTime(localtime);

    }
}
