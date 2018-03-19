package com.ryanair.api.biz;

import com.ryanair.api.model.flightRoute.FlightRouteParameters;
import com.ryanair.api.model.interconnections.Interconnections;
import com.ryanair.api.model.interconnections.InterconnectionsResult;
import com.ryanair.api.model.interconnections.Leg;
import com.ryanair.api.model.flightRoute.FlightRoute;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InterconnectionFactory {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    protected static FlightRouteParameters createFlightsRouteRequestScale(Interconnections interconnections, String intersection) {
        FlightRouteParameters flightRouteParameters = new FlightRouteParameters(interconnections.getDeparture(), intersection, interconnections.getArrival(), interconnections.getDepartureDateTime(), interconnections.getArrivalDateTime());
        return flightRouteParameters;
    }

    protected static FlightRouteParameters createFlightsRouteRequestDirectFly(Interconnections interconnections) {
        FlightRouteParameters flightRouteParameters = new FlightRouteParameters(interconnections.getDeparture(), interconnections.getArrival(), interconnections.getDepartureDateTime(), interconnections.getArrivalDateTime());
        return flightRouteParameters;
    }


    protected static List<InterconnectionsResult> createInterconnectionsResult(List<FlightRoute> validFlights, int numStops) {
        List<InterconnectionsResult> interconnectionsResultList = new ArrayList<InterconnectionsResult>();
        int flightSize = validFlights.size();
        for (int i = 0; i < flightSize; i++) {
            FlightRoute flight = validFlights.get(i);
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

    private static Leg createLeg(FlightRoute flight) {
        Leg leg = new Leg();
        leg.setArrivalDateTime(flight.getArrivalDateTime().format(formatter));
        leg.setDepartureDateTime(flight.getDepartureDateTime().format(formatter));
        leg.setArrivalAirport(flight.getArrival());
        leg.setDepartureAirport(flight.getDeparture());
        return leg;
    }
}
