package com.ryanair.api.biz;


import com.ryanair.api.model.Interconnections.Interconnections;
import com.ryanair.api.model.route.Route;
import com.ryanair.api.service.rest.consumer.RoutesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoutesBizImpl implements RoutesBiz {

    @Autowired
    private RoutesService routesService;

    private static final Logger LOG = LoggerFactory.getLogger(RoutesBizImpl.class);

    public boolean isValidRoute(Interconnections interconnections) {
        Map<String, Route> routeMap = getRoutesMap();
        return routeMap.containsKey(getRouteKey(interconnections.getDeparture(),interconnections.getArrival()));
    }

    public Map<String, Route> getRoutesMap() {
        List<Route> routeList = routesService.gelAllRoutes();
        Map<String, Route> routeMap = getDataRoutes(routeList);
        return routeMap;
    }

    public Set<String> getScalingPointForInterconnections(Interconnections interconnections) {
        List<Route> routeList = routesService.gelAllRoutes();
        Map<String, Set<String>>  mapDepartures = getMapDepartures(routeList);
        Map<String, Set<String>>  mapArrivals = getMapArrival(routeList);
        Set<String> departureSet = mapDepartures.get(interconnections.getDeparture());
        Set<String> arrivalSet = mapArrivals.get(interconnections.getArrival());
        Set<String> intersection = new HashSet<String>(departureSet);
        intersection.retainAll(arrivalSet);
        return intersection;
    }


    private static String getRouteKey(String departure, String arrival) {
        return departure + "-" + arrival;
    }

    private Map<String, Route> getDataRoutes(List<Route> routeList)  {
        Map<String, Route> routeMap =
                routeList.stream().filter(u -> u.getConnectingAirport() == null).collect(Collectors.toMap(Route::getRouteKey , route -> route));
        return routeMap;
    }

    private Map<String, Set<String>> getMapDepartures (List<Route> routeList)  {
        Map<String, Set<String>> mapDepartures = new HashMap<String, Set<String>>() ;
        for (Route route : routeList) {
            String departure = route.getAirportFrom();
            if (!mapDepartures.containsKey(departure)) {
                Set<String> arrivals = new HashSet<String>();
                arrivals.add(route.getAirportTo());
                mapDepartures.put(departure, arrivals);
            } else {
                mapDepartures.get(departure).add(route.getAirportTo());
            }
        }
        return mapDepartures;
    }

    private Map<String, Set<String>> getMapArrival (List<Route> routeList)  {
        Map<String, Set<String>> mapArrival = new HashMap<String, Set<String>>() ;
        for (Route route : routeList) {
            String arrival = route.getAirportTo();
            if (!mapArrival.containsKey(arrival)) {
                Set<String> departures = new HashSet<String>();
                departures.add(route.getAirportFrom());
                mapArrival.put(arrival, departures);
            } else {
                mapArrival.get(arrival).add(route.getAirportFrom());
            }
        }
        return mapArrival;
    }


    public void setRoutesService(RoutesService routesService) {
        this.routesService = routesService;
    }

}
