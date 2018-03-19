package com.ryanair.api.biz;

import com.ryanair.api.model.interconnections.Interconnections;
import com.ryanair.api.model.route.Route;

import java.util.Map;
import java.util.Set;

public interface RoutesBiz {

    //public Map<String, Route> getRoutesMap();

    public boolean isValidRoute(Interconnections interconnections);

    public boolean isValidDeparture(String departure);

    public boolean isValidArrival(String arrival);

    public Set<String> getIntermediateAirportsByRoute(Interconnections interconnections);

}
