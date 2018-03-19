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
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

@Test
public class RoutesServiceImplTest {


    @InjectMocks
    private RoutesServiceImpl routesService = new RoutesServiceImpl();

    @Mock
    private RoutesStore routesStore;



    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ResultRestStore<List<Route>> resultRestStore = new ResultRestStore<List<Route>>();
        Route route1 = createRoute("MAD", "BCN");
        Route route2 = createRoute("MAD", "PMI");
        Route route3 = createRoute("MAD", "IBZ");
        Route route4 = createRoute("MAD", "TFN");
        Route route5 = createRoute("BCN", "MAD");
        Route route6 = createRoute("BCN", "MAD");
        route6.setConnectingAirport("YES");
        List<Route> routeList = Arrays.asList(route1, route2, route3, route4, route5, route6);
        resultRestStore.setResult(routeList);
        when(routesStore.gelAllRoutes()).thenReturn(resultRestStore);
    }

    private Route createRoute(String desparture, String arrival) {
        Route route = new Route();
        route.setAirportFrom(desparture);
        route.setAirportTo(arrival);
        return route;
    }




    @Test
    public void routesServiceGeneralTest() {
        List<Route> routeList = routesService.getAllRoutes();
        assertTrue(!routeList.isEmpty());
        assertEquals(5, routeList.size());

    }


}
