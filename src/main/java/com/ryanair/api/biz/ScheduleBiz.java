package com.ryanair.api.biz;


import com.ryanair.api.model.flightRoute.FlightRouteParameters;
import com.ryanair.api.model.flightRoute.FlightRouteResponse;

import java.util.List;

public interface ScheduleBiz {

    //public List<ScheduleResponse> getSchedulesListForInterconnectionsParameters(FlightRouteParameters flightRoute);


    public FlightRouteResponse getFlightsByRoute(List<FlightRouteParameters> flightRouteParametersList);

    public FlightRouteResponse getFlightsByRoute(FlightRouteParameters flightRouteParameters);
}
