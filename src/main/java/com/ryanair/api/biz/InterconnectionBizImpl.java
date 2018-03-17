package com.ryanair.api.biz;

import com.ryanair.api.model.Interconnections.InterconnectionsResult;
import com.ryanair.api.model.Interconnections.Interconnections;
import com.ryanair.api.model.Interconnections.Leg;
import com.ryanair.api.model.Schedule.*;
import com.ryanair.api.model.Section;
import com.ryanair.api.service.rest.consumer.SchedulesServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterconnectionBizImpl implements InterconnectionBiz {

    @Autowired
    private ScheduleBiz scheduleBiz;
    @Autowired
    private RoutesBiz routesBiz;

    private static final Logger LOG = LoggerFactory.getLogger(InterconnectionBizImpl.class);



    @Override
    public List<InterconnectionsResult> getRoutes(Interconnections interconnections) {
        List<InterconnectionsResult> interconnectionsResultList = new ArrayList<InterconnectionsResult>();
        interconnectionsResultList.addAll(directFlyResult(interconnections));
        interconnectionsResultList.addAll(oneScaleResult(interconnections));
        return  interconnectionsResultList;

    }

    private List<InterconnectionsResult> oneScaleResult(Interconnections interconnections) {
        LOG.info("get routes with 1 scale");
        List<InterconnectionsResult> interconnectionsResultList = Collections.EMPTY_LIST;
        List<FlightResult> validFlightsListFinish = new ArrayList<FlightResult>();
        Set<String> intersectionSet = routesBiz.getScalingPointForInterconnections(interconnections);
        List<FlightRoute> flightRouteList =  createFlightsRouteRequest(interconnections, intersectionSet);
        FlightRouteResult  flightRouteResult = scheduleBiz.getFlightsByRoute(flightRouteList);

        for (FlightRoute flightRoute : flightRouteList) {
            List<Section> sectionList = flightRoute.getSections();
            String route1 = sectionList.get(0).getRoute();
            String route2 = sectionList.get(1).getRoute();

            if (flightRouteResult.getFlightListMap().containsKey(route1)
                    && flightRouteResult.getFlightListMap().containsKey(route2)) {

                List<FlightResult> flightResultListFirstScale = flightRouteResult.getFlighResultSection(route1);
                List<FlightResult> flightResultListSecondScale = flightRouteResult.getFlighResultSection(route2);
                validFlightsListFinish.addAll(filterByFirstSection(flightRoute, flightResultListFirstScale, flightResultListSecondScale));
            }

        }
        interconnectionsResultList = InterconnectionFactory.createInterconnectionsResult(validFlightsListFinish, 1);
        return interconnectionsResultList;
    }

    private List<FlightRoute> createFlightsRouteRequest(Interconnections interconnections, Set<String> intersectionSet) {
        List<FlightRoute> flightRouteList = new ArrayList<FlightRoute>();
        if (!intersectionSet.isEmpty()) {
            for(String intersection : intersectionSet) {
                FlightRoute flightRoute = createFlightsRouteRequestScale(interconnections, intersection);
                flightRouteList.add(flightRoute);
            }
        }
        return flightRouteList;
    }

    private List<FlightResult> filterByFirstSection(FlightRoute flightRoute, List<FlightResult> flightResultListFirstScale, List<FlightResult> flightResultListSecondScale) {
        List<FlightResult> flightResultReturn = new ArrayList<FlightResult>();

        List<FlightResult> flightResultListFirstScaleFilter = filterFlightListByTime(flightResultListFirstScale,flightRoute.getDepartureDateTime(), flightRoute.getArrivalDateTime());
        for (FlightResult flightResultFirst : flightResultListFirstScaleFilter) {
            LocalDateTime arrivalFirstSection = flightResultFirst.getArrivalDateTime();
            List<FlightResult> flightResultListSecondScaleFilter = filterFlightListByTime(flightResultListSecondScale, arrivalFirstSection.plusHours(2), flightRoute.getArrivalDateTime());
            if (!flightResultListSecondScaleFilter.isEmpty()) {
                for (FlightResult flightResultSecond : flightResultListSecondScaleFilter) {
                    flightResultReturn.add(flightResultFirst);
                    flightResultReturn.add(flightResultSecond);
                }
            }
        }
        return flightResultReturn;
    }



    private List<InterconnectionsResult>  directFlyResult(Interconnections interconnections) {
        LOG.info("get direct routes");
        List<InterconnectionsResult> interconnectionsResultList = Collections.EMPTY_LIST;
        if (routesBiz.isValidRoute(interconnections)) {
            FlightRoute flightRoute = createFlightsRouteRequestDirectFly(interconnections);
            FlightRouteResult  flightRouteResult = scheduleBiz.getFlightsByRoute(flightRoute);
            List<FlightResult> flightResultList = flightRouteResult.getFlighResultSection(flightRoute.getSections().get(0).getRoute());
            List<FlightResult> validFlightsList = filterFlightListByTime(flightResultList,interconnections.getDepartureDateTime(), interconnections.getArrivalDateTime()) ;
            interconnectionsResultList = InterconnectionFactory.createInterconnectionsResult(validFlightsList, 0);
        }
        return interconnectionsResultList;
    }


    private FlightRoute createFlightsRouteRequestScale(Interconnections interconnections, String intersection) {
        FlightRoute flightRoute = new FlightRoute(interconnections.getDeparture(), intersection, interconnections.getArrival(), interconnections.getDepartureDateTime(), interconnections.getArrivalDateTime());
        return flightRoute;
    }

    private FlightRoute createFlightsRouteRequestDirectFly(Interconnections interconnections) {
        FlightRoute flightRoute = new FlightRoute(interconnections.getDeparture(), interconnections.getArrival(), interconnections.getDepartureDateTime(), interconnections.getArrivalDateTime());
        return flightRoute;
    }

    private  List<FlightResult> filterFlightListByTime (List<FlightResult> flightList, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return flightList.stream().filter(u -> u.getDepartureDateTime().compareTo(departureDateTime) >= 0)
                .filter(u -> u.getArrivalDateTime().compareTo(arrivalDateTime) <= 0).collect(Collectors.toList());
    }

    public void setScheduleBiz(ScheduleBiz scheduleBiz) {
        this.scheduleBiz = scheduleBiz;
    }
    public void setRoutesBiz(RoutesBiz routesBiz) {
        this.routesBiz = routesBiz;
    }













}
