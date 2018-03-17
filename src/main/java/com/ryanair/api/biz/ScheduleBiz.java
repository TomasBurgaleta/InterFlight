package com.ryanair.api.biz;


import com.ryanair.api.model.Schedule.FlightResult;
import com.ryanair.api.model.Schedule.FlightRoute;
import com.ryanair.api.model.Schedule.FlightRouteResult;
import com.ryanair.api.model.Schedule.ScheduleResponse;

import java.util.List;

public interface ScheduleBiz {

    //public List<ScheduleResponse> getSchedulesListForInterconnectionsParameters(FlightRoute flightRoute);


    public FlightRouteResult getFlightsByRoute(List<FlightRoute> flightRouteList);

    public FlightRouteResult getFlightsByRoute(FlightRoute flightRoute);
}
