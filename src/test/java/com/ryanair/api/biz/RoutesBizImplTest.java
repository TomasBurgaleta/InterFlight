package com.ryanair.api.biz;

import com.ryanair.api.model.interconnections.Interconnections;
import com.ryanair.api.model.route.Route;
import com.ryanair.api.service.rest.consumer.RoutesService;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Test
public class RoutesBizImplTest {


    @InjectMocks
    private RoutesBiz routesBiz = new RoutesBizImpl();

    @Mock
    private RoutesService routesService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(routesService.getAllRoutes()).thenReturn(createListRoute());
    }

    @Test
    public void verifyOnlyOneServiceAndVerifyRoutes() {
        assertTrue(routesBiz.isValidDeparture("MAD")) ;
        assertTrue(routesBiz.isValidArrival("MAD")) ;
        assertFalse(routesBiz.isValidDeparture("IBZ"));
        Interconnections interconnections1 = createInterconnections("MAD", "BCN");
        Interconnections interconnections2 = createInterconnections("TFN", "BCN");
        assertTrue(routesBiz.isValidRoute(interconnections1));
        assertFalse(routesBiz.isValidRoute(interconnections2));
        Mockito.verify(routesService,times(1)).getAllRoutes();
    }

    public void verifyIntersectionsTest() {
        Interconnections interconnections1 = createInterconnections("BCN", "TFN");
        Set<String> intermediateAirports = routesBiz.getIntermediateAirportsByRoute(interconnections1);
        assertFalse(intermediateAirports.isEmpty());
        assertEquals(1, intermediateAirports.size());
        Mockito.verify(routesService,times(1)).getAllRoutes();
    }



    private Interconnections createInterconnections(String departure, String arrival) {
        Interconnections interconnections =new Interconnections();
        interconnections.setArrival(arrival);
        interconnections.setDeparture(departure);
        return interconnections;
    }

    private List<Route> createListRoute() {
        Route route1 = createRoute("MAD", "BCN");
        Route route2 = createRoute("MAD", "PMI");
        Route route3 = createRoute("MAD", "IBZ");
        Route route4 = createRoute("MAD", "TFN");
        Route route5 = createRoute("BCN", "MAD");
        return Arrays.asList(route1, route2, route3, route4, route5);
    }

    private Route createRoute(String desparture, String arrival) {
        Route route = new Route();
        route.setAirportFrom(desparture);
        route.setAirportTo(arrival);
        return route;
    }


}
