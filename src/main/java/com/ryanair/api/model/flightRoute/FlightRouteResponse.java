package com.ryanair.api.model.flightRoute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightRouteResponse {

    private Map<String, List<FlightRoute>> flightListMap;

    public FlightRouteResponse() {
        flightListMap = new HashMap<String, List<FlightRoute>>();
    }

    public void addFlightResultSection(String section, List<FlightRoute> flightResultList) {
        flightListMap.put(section, flightResultList);
    }

    public  List<FlightRoute> getFlightResultSection(String section) {
        return flightListMap.get(section);
    }

    public Map<String, List<FlightRoute>> getFlightListMap() {
        return flightListMap;
    }


}
