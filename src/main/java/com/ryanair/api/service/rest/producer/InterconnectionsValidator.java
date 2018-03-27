package com.ryanair.api.service.rest.producer;

import com.ryanair.api.biz.RoutesBiz;
import com.ryanair.api.model.interconnections.Interconnections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
@Component
public class InterconnectionsValidator implements Validator {

    @Autowired
    private RoutesBiz routesBiz;
    @Override
    public boolean supports(Class<?> aClass) {
        return Interconnections.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Interconnections interconnections = (Interconnections) o;
            if(!routesBiz.isValidDeparture(interconnections.getDeparture())) {
                errors.rejectValue("departure","","Invalid Departure");
            }
            if(!routesBiz.isValidArrival(interconnections.getArrival())){
                errors.rejectValue("arrival","","Invalid Arrival");
            }
            if (interconnections.getDepartureDateTime().isBefore(LocalDateTime.now())) {
                errors.rejectValue("departure","","Invalid departure, Date before today");
            }
        if ( interconnections.getArrivalDateTime().isBefore(LocalDateTime.now())) {
                errors.rejectValue("arrival","","Invalid arrival, Date before today");
        }
            if (interconnections.getDepartureDateTime().isAfter(interconnections.getArrivalDateTime())) {
                errors.rejectValue("departure","","\"Departure time is after Arrival time");
            }
        }
}




