package com.ryanair.api.service.rest.producer;

import com.ryanair.api.biz.InterconnectionException;
import com.ryanair.api.model.interconnections.Interconnections;
import com.ryanair.api.model.interconnections.InterconnectionsRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InterconnectionFactory {


    protected static Interconnections createInterconnections(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) throws InterconnectionException {
        try {
            Interconnections interconnections = new Interconnections(departure, arrival, departureDateTime, arrivalDateTime);
            return interconnections;
        } catch (DateTimeParseException e) {
                throw new InterconnectionException("Invalid DateTime");
        }
    }
}
