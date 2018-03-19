package com.ryanair.api.biz;


import com.ryanair.api.model.flightRoute.FlightRoute;
import com.ryanair.api.model.flightRoute.FlightRouteParameters;
import com.ryanair.api.model.flightRoute.FlightRouteResponse;
import com.ryanair.api.model.Section;
import com.ryanair.api.service.rest.consumer.SchedulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public FlightRouteResponse getFlightsByRoute(List<FlightRouteParameters> flightRouteParametersList) {
        FlightRouteResponse flightRouteResponse = new FlightRouteResponse();
        List<FlightRoute> validFlights;
        List<CompletableFuture<List<FlightRoute>>> futures = new ArrayList<CompletableFuture<List<FlightRoute>>>();
        LOG.info("getting all FlightRouteParameters for futures");
        try {
            for (FlightRouteParameters flightRouteParameters : flightRouteParametersList) {
                for (Section section: flightRouteParameters.getSections()) {
                    CompletableFuture<List<FlightRoute>> flightResultList = schedulesService.getFlightRouteListBySection(section, flightRouteParameters);
                    futures.add(flightResultList);
                }
            }
            LOG.info("finish all request for futures");
            sequence(futures).join();
            LOG.info("complete all request for futures");
            for (CompletableFuture<List<FlightRoute>> future: futures) {
                validFlights = future.get();
                if (!validFlights.isEmpty()) {
                    flightRouteResponse.addFlighResultSection(validFlights.get(0).getRoute(), validFlights);
                }
            }
        } catch (InterruptedException e) {
            LOG.error("Error creating futures", e);

        }  catch (ExecutionException e) {
            LOG.error("Error in execution of futures", e);
        }
        return flightRouteResponse;
    }

    @Override
    public FlightRouteResponse getFlightsByRoute(FlightRouteParameters flightRouteParameters) {
        FlightRouteResponse flightRouteResponse = new FlightRouteResponse();
        Section section = flightRouteParameters.getSections().get(0);
        List<FlightRoute> validFlights;
        CompletableFuture<List<FlightRoute>> future;
        try {
            future = schedulesService.getFlightRouteListBySection(section, flightRouteParameters);
            CompletableFuture.allOf(future).join();
            validFlights = future.get();
            flightRouteResponse.addFlighResultSection(validFlights.get(0).getRoute(), validFlights);
        }catch (InterruptedException e) {
            LOG.error("Error creating futures", e);

        }  catch (ExecutionException e) {
            LOG.error("Error in execution of futures", e);
        }

        return flightRouteResponse;
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
