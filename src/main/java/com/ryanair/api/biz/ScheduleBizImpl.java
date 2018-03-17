package com.ryanair.api.biz;


import com.ryanair.api.model.Schedule.*;
import com.ryanair.api.model.Section;
import com.ryanair.api.service.rest.consumer.SchedulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ScheduleBizImpl  implements ScheduleBiz{

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleBizImpl.class);

    @Autowired
    private SchedulesService schedulesService;

    @Override
    public FlightRouteResult getFlightsByRoute(List<FlightRoute> flightRouteList) {
        FlightRouteResult flightRouteResult = new FlightRouteResult();
        List<FlightResult> validFlights;
        List<CompletableFuture<List<FlightResult>>> futures = new ArrayList<CompletableFuture<List<FlightResult>>>();
        LOG.info("getting all FlightRoute for futures");
        try {
            for (FlightRoute flightRoute : flightRouteList) {
                for (Section section: flightRoute.getSections()) {
                    CompletableFuture<List<FlightResult>> flightResultList = schedulesService.getFlightsBySection(section, flightRoute);
                    futures.add(flightResultList);
                }
            }
            LOG.info("finish all request for futures");
            sequence(futures).join();
            LOG.info("complete all request for futures");
            for (CompletableFuture<List<FlightResult>> future: futures) {
                validFlights = future.get();
                if (!validFlights.isEmpty()) {
                    flightRouteResult.addFlighResultSection(validFlights.get(0).getRoute(), validFlights);
                }
            }
        } catch (InterruptedException e) {
            LOG.error("Error creating futures", e);

        }  catch (ExecutionException e) {
            LOG.error("Error in execution of futures", e);
        }
        return flightRouteResult;
    }

    @Override
    public FlightRouteResult getFlightsByRoute(FlightRoute flightRoute) {
        FlightRouteResult flightRouteResult = new FlightRouteResult();
        Section section = flightRoute.getSections().get(0);
        List<FlightResult> validFlights;
        CompletableFuture<List<FlightResult>> future;
        try {
            future = schedulesService.getFlightsBySection(section, flightRoute);
            CompletableFuture.allOf(future).join();
            validFlights = future.get();
            flightRouteResult.addFlighResultSection(validFlights.get(0).getRoute(), validFlights);
        }catch (InterruptedException e) {
            LOG.error("Error creating futures", e);

        }  catch (ExecutionException e) {
            LOG.error("Error in execution of futures", e);
        }

        return flightRouteResult;
    }


    private <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v ->
                futures.stream().
                        map(future -> future.join()).
                        collect(Collectors.<T>toList())
        );
    }

}
