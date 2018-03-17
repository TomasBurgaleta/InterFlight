package com.ryanair.api.biz;

import com.ryanair.api.model.Interconnections.InterconnectionsResult;
import com.ryanair.api.model.Interconnections.Leg;
import com.ryanair.api.model.Schedule.FlightResult;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InterconnectionFactory {

     static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


    protected static List<InterconnectionsResult> createInterconnectionsResult(List<FlightResult> validFlights, int numStops) {
        List<InterconnectionsResult> interconnectionsResultList = new ArrayList<InterconnectionsResult>();
        int flightSize = validFlights.size();
        for (int i = 0; i < flightSize; i++) {
            FlightResult flight = validFlights.get(i);
            InterconnectionsResult interconnectionsResult = new InterconnectionsResult();
            interconnectionsResult.setStops(numStops);
            List<Leg> legList = new ArrayList<Leg>();
            legList.add(createLeg(flight));
            if (numStops == 1 && flightSize > (i+1)) {
                i++;
                flight = validFlights.get(i);
                legList.add(createLeg(flight));
            }
            interconnectionsResult.setLegs(legList);
            interconnectionsResultList.add(interconnectionsResult);
        }
        return interconnectionsResultList;
    }

    private static Leg createLeg(FlightResult flight) {
        Leg leg = new Leg();
        leg.setArrivalDateTime(flight.getArrivalDateTime().format(formatter));
        leg.setDepartureDateTime(flight.getDepartureDateTime().format(formatter));
        leg.setArrivalAirport(flight.getArrival());
        leg.setDepartureAirport(flight.getDeparture());
        return leg;
    }
}
