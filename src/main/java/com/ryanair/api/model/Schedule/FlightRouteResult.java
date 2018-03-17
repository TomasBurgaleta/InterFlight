package com.ryanair.api.model.Schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightRouteResult {

    private Map<String, List<FlightResult>> flightListMap;

    public FlightRouteResult() {
        flightListMap = new HashMap<String, List<FlightResult>>();
    }

    public void addFlighResultSection(String section, List<FlightResult> flightResultlist) {
        flightListMap.put(section, flightResultlist);
    }

    public  List<FlightResult>  getFlighResultSection(String section) {
        return flightListMap.get(section);
    }

    public Map<String, List<FlightResult>> getFlightListMap() {
        return flightListMap;
    }


}
