package com.ryanair.api.biz;

import com.ryanair.api.model.Section;
import com.ryanair.api.model.flightRoute.FlightRoute;
import com.ryanair.api.model.flightRoute.FlightRouteParameters;
import com.ryanair.api.model.flightRoute.FlightRouteResponse;
import com.ryanair.api.model.interconnections.Interconnections;
import com.ryanair.api.model.interconnections.InterconnectionsResult;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertFalse;

@Test
public class InterconnectionBizImplTest {

    @InjectMocks
    private InterconnectionBiz interconnectionBiz = new InterconnectionBizImpl();

    @Mock
    private ScheduleBiz scheduleBiz;
    @Mock
    private RoutesBiz routesBiz;

    private Section sectionMADBCN = new Section("MAD", "BCN");
    private Interconnections interconnections;
    private LocalDateTime departureTime = LocalDateTime.now().withDayOfMonth(1);
    private LocalDateTime arrivalTime = LocalDateTime.now().withDayOfMonth(1).plusDays(15);

    @BeforeMethod
    public void setUp() throws InterruptedException {
        MockitoAnnotations.initMocks(this);
        interconnections = createInterconnections(sectionMADBCN);
        when(routesBiz.isValidRoute(interconnections)).thenReturn(true);


    }


    @Test
    public void interconnectionsByRoutesTest() {
        FlightRouteResponse flightRouteResponse = new FlightRouteResponse();
        FlightRoute flightRoute = createFlightRoute(sectionMADBCN);
        List<FlightRoute> flightResultList = Arrays.asList(flightRoute);
        flightRouteResponse.addFlightResultSection(sectionMADBCN.getRoute(), flightResultList);
        FlightRouteParameters flightRouteParameters = InterconnectionFactory.createFlightsRouteRequestDirectFly(interconnections);
        when(scheduleBiz.getFlightsByRoute(flightRouteParameters)).thenReturn(flightRouteResponse);
        List<InterconnectionsResult> interconnectionsResultList = interconnectionBiz.getInterconnectionsByRoutes(interconnections);
        assertFalse(interconnectionsResultList.isEmpty());
    }

    private FlightRoute createFlightRoute(Section section ) {
        FlightRoute flightRoute = new FlightRoute(departureTime.toLocalDate());
        flightRoute.setDeparture(section.getDeparture());
        flightRoute.setArrival(section.getArrival());
        flightRoute.setArrivalDateTime(departureTime.plusHours(2));
        flightRoute.setDepartureDateTime(departureTime.plusHours(1));
        return flightRoute;
    }



    private Interconnections createInterconnections(Section section) {
        Interconnections interconnections = new Interconnections(section.getDeparture(), section.getArrival(), departureTime, arrivalTime);
        return interconnections;
    }


}
