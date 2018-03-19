package com.ryanair.api.biz;

import com.ryanair.api.model.Section;
import com.ryanair.api.model.flightRoute.FlightRoute;
import com.ryanair.api.model.flightRoute.FlightRouteParameters;
import com.ryanair.api.model.flightRoute.FlightRouteResponse;
import com.ryanair.api.model.schedule.Flight;
import com.ryanair.api.service.rest.consumer.RoutesService;
import com.ryanair.api.service.rest.consumer.SchedulesService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

@Test
public class ScheduleBizImplTest {


    @InjectMocks
    private ScheduleBiz scheduleBiz = new ScheduleBizImpl();

    @Mock
    private SchedulesService schedulesService;

    private CompletableFuture<List<FlightRoute>> future;
    FlightRouteParameters flightRouteParameters;

    @BeforeMethod
    public void setUp() throws InterruptedException {
        MockitoAnnotations.initMocks(this);
        LocalDateTime departureTime = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime arrivalTime = LocalDateTime.now().withDayOfMonth(1).plusDays(15);
        //FlightRouteParameters flightRouteParameters = new FlightRouteParameters("MAD", "BCN", departureTime, arrivalTime);



    }

    @Test
    public void getFlightsByRouteTest() throws InterruptedException {
        LocalDateTime departureTime = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime arrivalTime = LocalDateTime.now().withDayOfMonth(1).plusDays(15);
        List<FlightRoute> validFlights = new ArrayList<FlightRoute>();
        Flight flight = new Flight("1926", "09:45", "13:20");
        FlightRoute flightRoute = new FlightRoute(flight, departureTime.toLocalDate(),"MAD", "BCN");
        validFlights.add(flightRoute);
        future = CompletableFuture.completedFuture(validFlights);
        FlightRouteParameters flightRouteParameters = new FlightRouteParameters("MAD", "BCN", departureTime, arrivalTime);
        when(schedulesService.getFlightRouteListBySection(any(Section.class), eq(flightRouteParameters))).thenReturn(future);
        //future.isDone();
        FlightRouteResponse flightRouteResponse = scheduleBiz.getFlightsByRoute(flightRouteParameters);
        assertNotNull(flightRouteResponse);
    }

}
