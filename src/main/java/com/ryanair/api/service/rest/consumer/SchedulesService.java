package com.ryanair.api.service.rest.consumer;


import com.ryanair.api.model.Schedule.FlightResult;
import com.ryanair.api.model.Schedule.FlightRoute;
import com.ryanair.api.model.Schedule.ScheduleParameters;
import com.ryanair.api.model.Schedule.ScheduleResponse;
import com.ryanair.api.model.Section;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SchedulesService {

    public CompletableFuture<List<FlightResult>> getFlightsBySection(Section section, FlightRoute flightRoute) throws InterruptedException;

}
