package com.ryanair.api.model.flightRoute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightRouteResponse {

    private Map<String, List<FlightRoute>> flightListMap;

    public FlightRouteResponse() {
        flightListMap = new HashMap<String, List<FlightRoute>>();
    }

    public void addFlighResultSection(String section, List<FlightRoute> flightResultlist) {
        flightListMap.put(section, flightResultlist);
    }

    public  List<FlightRoute>  getFlighResultSection(String section) {
        return flightListMap.get(section);
    }

    public Map<String, List<FlightRoute>> getFlightListMap() {
        return flightListMap;
    }


}
