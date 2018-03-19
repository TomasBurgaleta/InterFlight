package com.ryanair.api.biz;

import com.ryanair.api.model.Section;
import com.ryanair.api.model.flightRoute.FlightRoute;
import com.ryanair.api.model.flightRoute.FlightRouteParameters;
import com.ryanair.api.model.flightRoute.FlightRouteResponse;
import com.ryanair.api.model.schedule.Flight;
import com.ryanair.api.service.rest.consumer.SchedulesService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

@Test
public class ScheduleBizImplTest {


    @InjectMocks
    private ScheduleBiz scheduleBiz = new ScheduleBizImpl();

    @Mock
    private SchedulesService schedulesService;
    @Mock
    private InterruptedException interruptedException;
    @Mock
    private ExecutionException executionException;

    private Section sectionMADBCN = new Section("MAD", "BCN");
    private Section sectionBCNPMI = new Section("BCN", "PMI");

    @BeforeMethod
    public void setUp() throws InterruptedException {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void getFlightsByRouteListTest() throws InterruptedException {
        LocalDateTime departureTime = LocalDateTime.now().withDayOfMonth(1);
        List<FlightRoute> validFlights = createFlightsRouteList(sectionMADBCN, departureTime);
        CompletableFuture<List<FlightRoute>> futureMADBCN = CompletableFuture.completedFuture(validFlights);
        List<FlightRoute> validFlights2 = createFlightsRouteList(sectionBCNPMI, departureTime);
        CompletableFuture<List<FlightRoute>> futureBCNMAD = CompletableFuture.completedFuture(validFlights2);
        FlightRouteParameters flightRouteParameters = createParameter(sectionMADBCN);
        FlightRouteParameters flightRouteParameters2 = createParameter(sectionBCNPMI);
        List<FlightRouteParameters> flightRouteParametersList = Arrays.asList(flightRouteParameters, flightRouteParameters2);
        when(schedulesService.getFlightRouteListBySection(eq(sectionMADBCN), eq(flightRouteParameters))).thenReturn(futureMADBCN);
        when(schedulesService.getFlightRouteListBySection(eq(sectionBCNPMI), eq(flightRouteParameters2))).thenReturn(futureBCNMAD);
        FlightRouteResponse flightRouteResponse = scheduleBiz.getFlightsByRoute(flightRouteParametersList);
        assertNotNull(flightRouteResponse);
        Map<String, List<FlightRoute>> flightListMap =flightRouteResponse.getFlightListMap();
        assertNotNull(flightListMap.get(sectionMADBCN.getRoute()));
        assertNotNull(flightListMap.get(sectionBCNPMI.getRoute()));
    }


    @Test
    public void getFlightsByRouteTest() throws InterruptedException {
        LocalDateTime departureTime = LocalDateTime.now().withDayOfMonth(1);
        List<FlightRoute> validFlights = createFlightsRouteList(sectionMADBCN, departureTime);
        CompletableFuture<List<FlightRoute>> future = CompletableFuture.completedFuture(validFlights);
        FlightRouteParameters flightRouteParameters = createParameter(sectionMADBCN);
        when(schedulesService.getFlightRouteListBySection(eq(sectionMADBCN), eq(flightRouteParameters))).thenReturn(future);
        FlightRouteResponse flightRouteResponse = scheduleBiz.getFlightsByRoute(flightRouteParameters);
        assertNotNull(flightRouteResponse);
        Map<String, List<FlightRoute>> flightListMap =flightRouteResponse.getFlightListMap();
        assertNotNull(flightListMap.get(sectionMADBCN.getRoute()));
        assertNull(flightListMap.get(sectionBCNPMI.getRoute()));
    }

    public void getFlightsByRouteInterruptedExceptionTest() throws InterruptedException {
        FlightRouteParameters flightRouteParameters = createParameter(sectionMADBCN);
        when(schedulesService.getFlightRouteListBySection(any(Section.class), any(FlightRouteParameters.class))).thenThrow(interruptedException);
        FlightRouteResponse flightRouteResponse = scheduleBiz.getFlightsByRoute(flightRouteParameters);
        assertNotNull(flightRouteResponse);
        Map<String, List<FlightRoute>> flightListMap =flightRouteResponse.getFlightListMap();
        assertNull(flightListMap.get(sectionMADBCN.getRoute()));
    }


    private FlightRouteParameters createParameter(Section section) {
        LocalDateTime departureTime = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime arrivalTime = LocalDateTime.now().withDayOfMonth(1).plusDays(15);
        return  new FlightRouteParameters(section.getDeparture(), section.getArrival(), departureTime, arrivalTime);
    }

    private List<FlightRoute> createFlightsRouteList(Section section, LocalDateTime departureTime) {
        List<FlightRoute> validFlights = new ArrayList<FlightRoute>();
        Flight flight = new Flight("1926", "09:45", "13:20");
        FlightRoute flightRoute = new FlightRoute(flight, departureTime.toLocalDate(),section.getDeparture(), section.getArrival());
        validFlights.add(flightRoute);
        return validFlights;
    }

}
