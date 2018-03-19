package com.ryanair.api.service.rest.producer;

import com.ryanair.api.biz.InterconnectionException;
import com.ryanair.api.model.interconnections.Interconnections;
import com.ryanair.api.model.interconnections.InterconnectionsRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InterconnectionFactory {



    protected static InterconnectionsRequest createRequestBean(String departure, String arrival, String departureDateTime, String arrivalDateTime) {
        InterconnectionsRequest interconnectionsRequest = new InterconnectionsRequest();
        interconnectionsRequest.setDeparture(departure);
        interconnectionsRequest.setArrival(arrival);
        interconnectionsRequest.setDepartureDateTime(departureDateTime);
        interconnectionsRequest.setArrivalDateTime(arrivalDateTime);
        return interconnectionsRequest;
    }

    protected static Interconnections createInterconnections(InterconnectionsRequest interconnectionsRequest) throws InterconnectionException {
        Interconnections interconnections = new Interconnections();
        try {
            interconnections.setArrival(interconnectionsRequest.getArrival());
            interconnections.setDeparture(interconnectionsRequest.getDeparture());
            interconnections.setArrivalDateTime(LocalDateTime.parse(interconnectionsRequest.getArrivalDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            interconnections.setDepartureDateTime(LocalDateTime.parse(interconnectionsRequest.getDepartureDateTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        catch (DateTimeParseException e) {
            throw new InterconnectionException("Invalid DateTime");
        }

        return interconnections;
    }
}
