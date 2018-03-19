package com.ryanair.api.service.rest.consumer;


import com.ryanair.api.model.flightRoute.FlightRoute;
import com.ryanair.api.model.flightRoute.FlightRouteParameters;
import com.ryanair.api.model.Section;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SchedulesService {

    public CompletableFuture<List<FlightRoute>> getFlightRouteListBySection(Section section, FlightRouteParameters flightRouteParameters) throws InterruptedException;

}
