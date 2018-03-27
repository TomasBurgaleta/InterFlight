package com.ryanair.api.model.interconnections;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class Interconnections {

    @NotNull
    @Size(min = 3, max = 3, message = "Not valid IATA")
    private String departure;
    @NotNull
    @Size(min = 3, max = 3, message = "Not valid IATA")
    private String arrival;
    @NotNull
    private LocalDateTime departureDateTime;
    @NotNull
    private LocalDateTime arrivalDateTime;

    public Interconnections(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        this.departure = departure;
        this.arrival = arrival;
        this.departureDateTime = departureDateTime;
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
}
