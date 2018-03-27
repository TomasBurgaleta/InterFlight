package com.ryanair.api.service.rest.consumer;

import com.ryanair.api.model.ResultRestStore;
import com.ryanair.api.model.route.Route;
import com.ryanair.api.model.schedule.Schedule;
import com.ryanair.api.store.rest.consumer.RoutesStore;
import com.ryanair.api.store.rest.consumer.SchedulesStore;
import com.ryanair.api.store.rest.consumer.SchedulesStoreImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

@Test
public class RoutesServiceImplTest {


    @InjectMocks
    private RoutesServiceImpl routesService = new RoutesServiceImpl();

    @Mock
    private RoutesStore routesStore;

    private static final String FR = "RYANAIR";



    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void routesServiceGeneralTest() {
        when(routesStore.gelAllRoutes()).thenReturn(createResultRestStore(createListRoutesWithoutConnectingAirports()));
        List<Route> routeList = routesService.getAllRoutes();
        assertTrue(!routeList.isEmpty());
        assertEquals(createListRoutesWithoutConnectingAirports().size(), routeList.size());
    }

    @Test
    public void routesWithConnectingAirportTest() {
        when(routesStore.gelAllRoutes()).thenReturn(createResultRestStore(createCompleteListRoutes()));
        List<Route> routeList = routesService.getAllRoutes();
        assertTrue(!routeList.isEmpty());
        assertNotEquals(createCompleteListRoutes().size(), routeList.size());
    }

    @Test
    public void routesByOperatorTest() {
        when(routesStore.gelAllRoutes()).thenReturn(createResultRestStore(createCompleteListRoutes()));
        List<Route> routeList = routesService.getRoutesByOperator(FR);
        assertTrue(!routeList.isEmpty());
        assertNotEquals(createCompleteListRoutes().size(), routeList.size());
    }



    private ResultRestStore<List<Route>> createResultRestStore(List<Route> routeList) {
        ResultRestStore<List<Route>> resultRestStore = new ResultRestStore<List<Route>>();
        resultRestStore.setResult(routeList);
        return resultRestStore;
    }

    private  List<Route> createListRoutesWithoutConnectingAirports() {
        Route route1 = createRoute("MAD", "BCN");
        Route route2 = createRoute("MAD", "PMI");
        Route route3 = createRoute("MAD", "IBZ");
        Route route4 = createRoute("MAD", "TFN");
        Route route5 = createRoute("BCN", "MAD");
        Route route6 = createRoute("BCN", "MAD");
        return Arrays.asList(route1, route2, route3, route4, route5, route6);
    }


    private Route createRoute(String departure, String arrival) {
        Route route = new Route();
        route.setAirportFrom(departure);
        route.setAirportTo(arrival);
        return route;
    }
    private  List<Route> createCompleteListRoutes() {
        Route route1 = createCompleteRoute("MAD", "BCN", null, false, false, "RYANAIR", "DOMESTIC");
        Route route2 = createCompleteRoute("MAD", "PMI", null, false, false, "RYANAIR", "LEISURE");
        Route route3 = createCompleteRoute("MAD", "IBZ", null, true, false, "RYANAIR", "GENERIC");
        Route route4 = createCompleteRoute("MAD", "TFN", null, false, false, "AIR_EUROPA", "GENERIC");
        Route route5 = createCompleteRoute("BCN", "MAD", null, false, true, "RYANAIR", "CITY");
        Route route6 = createCompleteRoute("BCN", "MAD", "AAA", false, false, "RYANAIR", "DOMESTIC");
        return Arrays.asList(route1, route2, route3, route4, route5, route6);
    }

    private Route createCompleteRoute(String departure, String arrival,String connectingAirport, boolean newRoute, boolean seasonalRoute, String operator , String group) {
        Route route = new Route();
        route.setAirportFrom(departure);
        route.setAirportTo(arrival);
        route.setConnectingAirport(connectingAirport);
        route.setNewRoute(newRoute);
        route.setSeasonalRoute(seasonalRoute);
        route.setOperator(operator);
        route.setGroup(group);
        return route;
    }





}
