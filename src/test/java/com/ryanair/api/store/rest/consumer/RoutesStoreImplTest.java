package com.ryanair.api.store.rest.consumer;

import com.ryanair.api.model.ResultRestStore;
import com.ryanair.api.model.route.Route;

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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

@Test
//@ContextConfiguration(locations = { "classpath:spring-test-config.xml" })
public class RoutesStoreImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RoutesStoreImpl routesStoreImpl = new RoutesStoreImpl();




    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Route route1 = new Route();
        Route route2 = new Route();
        Route[] routes = new Route[]{route1, route2};
        ResponseEntity<List<Route>> routeResponse;
        List<Route> routeList = Arrays.asList(route1, route2);
        routeResponse = new ResponseEntity(routeList, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), eq(new ParameterizedTypeReference<List<Route>>() {}))).thenReturn(routeResponse);

       // mockServer = MockRestServiceServer.createServer(restTemplate);


    }

    @Test
    public void readFlightRoutesTest() {
        ResultRestStore<List<Route>> resultRoute = routesStoreImpl.gelAllRoutes();
        List<Route> routeList = resultRoute.getResult();
        assertTrue(!routeList.isEmpty());
        assertEquals(2, routeList.size());
    }
}
