package com.ryanair.api.biz;

import com.ryanair.api.model.flightRoute.FlightRoute;
import com.ryanair.api.model.flightRoute.FlightRouteParameters;
import com.ryanair.api.model.flightRoute.FlightRouteResponse;
import com.ryanair.api.model.interconnections.InterconnectionsResult;
import com.ryanair.api.model.interconnections.Interconnections;
import com.ryanair.api.model.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public List<InterconnectionsResult> getInterconnectionsByRoutes(Interconnections interconnections) {
        List<InterconnectionsResult> interconnectionsResultList = new ArrayList<InterconnectionsResult>();
        interconnectionsResultList.addAll(directFlySearch(interconnections));
        interconnectionsResultList.addAll(oneScaleSearch(interconnections));
        return interconnectionsResultList;

    }

    private List<InterconnectionsResult> oneScaleSearch(Interconnections interconnections) {
        LOG.info("get routes with 1 scale");
        Set<String> intersectionSet = routesBiz.getIntermediateAirportsByRoute(interconnections);
        List<FlightRouteParameters> flightRouteParametersList =  getAllFlightRoutesParametersWithOneScale(interconnections, intersectionSet);
        FlightRouteResponse flightRouteResponse = scheduleBiz.getFlightsByRoute(flightRouteParametersList);
        List<FlightRoute> validFlightsList = filterValidOneScaleRoutes(flightRouteParametersList, flightRouteResponse);
        return InterconnectionFactory.createInterconnectionsResult(validFlightsList, 1);
    }


    private List<FlightRoute> filterValidOneScaleRoutes(List<FlightRouteParameters> flightRouteParametersList, FlightRouteResponse flightRouteResponse) {
        List<FlightRoute> validFlightsList = new ArrayList<FlightRoute>();
        for (FlightRouteParameters flightRouteParameters : flightRouteParametersList) {
            List<Section> sectionList = flightRouteParameters.getSections();
            String section1 = sectionList.get(0).getRoute();
            String section2 = sectionList.get(1).getRoute();
            if (flightRouteResponse.getFlightListMap().containsKey(section1)
                    && flightRouteResponse.getFlightListMap().containsKey(section2)) {

                List<FlightRoute> flightRouteListFirstScale = flightRouteResponse.getFlightResultSection(section1);
                List<FlightRoute> flightRouteListSecondScale = flightRouteResponse.getFlightResultSection(section2);
                validFlightsList.addAll(filterFlightsByInterconnectionTime(flightRouteParameters, flightRouteListFirstScale, flightRouteListSecondScale));
            }
        }
        return validFlightsList;
    }


    private List<FlightRouteParameters> getAllFlightRoutesParametersWithOneScale(Interconnections interconnections, Set<String> intersectionSet) {
        List<FlightRouteParameters> flightRouteParametersList = new ArrayList<FlightRouteParameters>();
        if (!intersectionSet.isEmpty()) {
            for(String intersection : intersectionSet) {
                FlightRouteParameters flightRouteParameters = InterconnectionFactory.createFlightsRouteRequestScale(interconnections, intersection);
                flightRouteParametersList.add(flightRouteParameters);
            }
        }
        return flightRouteParametersList;
    }

    private List<FlightRoute> filterFlightsByInterconnectionTime(FlightRouteParameters flightRouteParameters, List<FlightRoute> flightRouteListFirstScale, List<FlightRoute> flightRouteListSecondScale) {
        List<FlightRoute> flightRouteReturn = new ArrayList<FlightRoute>();

        List<FlightRoute> flightRouteListFirstScaleFilter = filterFlightByTime(flightRouteListFirstScale, flightRouteParameters.getDepartureDateTime(), flightRouteParameters.getArrivalDateTime());
        for (FlightRoute flightRouteFirst : flightRouteListFirstScaleFilter) {
            LocalDateTime arrivalFirstSection = flightRouteFirst.getArrivalDateTime();
            List<FlightRoute> flightRouteListSecondScaleFilter = filterFlightByTime(flightRouteListSecondScale, arrivalFirstSection.plusHours(2), flightRouteParameters.getArrivalDateTime());
            if (!flightRouteListSecondScaleFilter.isEmpty()) {
                for (FlightRoute flightRouteSecond : flightRouteListSecondScaleFilter) {
                    flightRouteReturn.add(flightRouteFirst);
                    flightRouteReturn.add(flightRouteSecond);
                }
            }
        }
        return flightRouteReturn;
    }


    private List<InterconnectionsResult> directFlySearch(Interconnections interconnections) {
        LOG.info("get direct routes");
        List<InterconnectionsResult> interconnectionsResultList = Collections.EMPTY_LIST;
        if (routesBiz.isValidRoute(interconnections)) {
            FlightRouteParameters flightRouteParameters = InterconnectionFactory.createFlightsRouteRequestDirectFly(interconnections);
            FlightRouteResponse flightRouteResponse = scheduleBiz.getFlightsByRoute(flightRouteParameters);
            List<FlightRoute> flightRouteList = flightRouteResponse.getFlightResultSection(flightRouteParameters.getSections().get(0).getRoute());
            List<FlightRoute> validFlightsList = filterFlightByTime(flightRouteList,interconnections.getDepartureDateTime(), interconnections.getArrivalDateTime()) ;
            interconnectionsResultList = InterconnectionFactory.createInterconnectionsResult(validFlightsList, 0);
        }
        return interconnectionsResultList;
    }


    private List<FlightRoute> filterFlightByTime(List<FlightRoute> flightList, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
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
