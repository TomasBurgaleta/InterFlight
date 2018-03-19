package com.ryanair.api.biz;


import com.ryanair.api.model.interconnections.Interconnections;
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

    private static List<Route> routeList = null;

    private static  Map<String, Route> routeMap = null;

    @Autowired
    private RoutesService routesService;

    private static final Logger LOG = LoggerFactory.getLogger(RoutesBizImpl.class);

    @Override
    public boolean isValidRoute(Interconnections interconnections) {
        Map<String, Route> routeMap = getRoutesMap();
        return routeMap.containsKey(getRouteKey(interconnections.getDeparture(),interconnections.getArrival()));
    }

    @Override
    public boolean isValidDeparture(String departure) {
        Map<String, Set<String>>  mapDepartures = getAllDepartures(gelAllRoutes());
        return mapDepartures.containsKey(departure);
    }

    @Override
    public boolean isValidArrival(String arrival) {
        Map<String, Set<String>>  mapArrivals = getAllArrival(gelAllRoutes());
        return mapArrivals.containsKey(arrival);
    }


    private Map<String, Route> getRoutesMap() {
        if (null == routeMap) {
            routeMap = getRouteMap(gelAllRoutes());
        }
        return routeMap;
    }

     private List<Route> gelAllRoutes() {
        if (null == routeList) {
            routeList = getRoutesFromRoutesService();
        }
        return routeList;
    }

    private synchronized List<Route> getRoutesFromRoutesService() {
        return routesService.getAllRoutes();
    }

    @Override
    public Set<String> getIntermediateAirportsByRoute(Interconnections interconnections) {
        LOG.info("loking for interconnected flights for route " + getRouteKey(interconnections.getArrival(),interconnections.getDeparture()));
        List<Route> routeList = routesService.getAllRoutes();
        Map<String, Set<String>>  mapDepartures = getAllDepartures(routeList);
        Map<String, Set<String>>  mapArrivals = getAllArrival(routeList);
        Set<String> departureSet = mapDepartures.get(interconnections.getDeparture());
        Set<String> arrivalSet = mapArrivals.get(interconnections.getArrival());
        Set<String> intersection = new HashSet<String>(departureSet);
        intersection.retainAll(arrivalSet);
        LOG.info("Scale in " + Arrays.toString(intersection.toArray()));
        return intersection;
    }


    private static String getRouteKey(String departure, String arrival) {
        return departure + "-" + arrival;
    }

    private Map<String, Route> getRouteMap(List<Route> routeList)  {
        Map<String, Route> routeMap =
                routeList.stream().collect(Collectors.toMap(Route::getRouteKey , route -> route));
        return routeMap;
    }

    private Map<String, Set<String>> getAllDepartures(List<Route> routeList)  {
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

    private Map<String, Set<String>> getAllArrival(List<Route> routeList)  {
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
