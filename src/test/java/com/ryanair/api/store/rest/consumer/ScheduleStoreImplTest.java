package com.ryanair.api.store.rest.consumer;

import com.ryanair.api.model.ResultRestStore;
import com.ryanair.api.model.route.Route;
import com.ryanair.api.model.schedule.ScheduleParameters;
import com.ryanair.api.model.schedule.Schedule;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;


@Test
public class ScheduleStoreImplTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RestClientException restClientException;
    @InjectMocks
    private SchedulesStoreImpl schedulesStore = new SchedulesStoreImpl();




    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Schedule schedule = new Schedule();
        ResponseEntity<Schedule> routeResponse;
        routeResponse = new ResponseEntity(schedule, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), eq(new ParameterizedTypeReference<Schedule>() {}))).thenReturn(routeResponse);
    }

    @Test
    public void readFlightRoutesTest() {
        ScheduleParameters scheduleParameters = new ScheduleParameters("MAD", "BCN", 2018, 5);
        ResultRestStore<Schedule> resultRoute = schedulesStore.getSchedules(scheduleParameters);
        Schedule schedule = resultRoute.getResult();
        assertNotNull(schedule);
        //assertEquals(1, routeList.size());
    }

    @Test
    public void readFlightRoutesRestClientExceptionTest() {
        ScheduleParameters scheduleParameters = new ScheduleParameters("MAD", "BCN", 2018, 5);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), eq(new ParameterizedTypeReference<Schedule>() {}))).thenThrow(restClientException);
        when(restClientException.getMessage()).thenReturn("ERROR");
        ResultRestStore<Schedule> resultRoute = schedulesStore.getSchedules(scheduleParameters);
        verify(restClientException, times(2)).getMessage();
        assertNull(resultRoute.getResult());
        assertTrue(!resultRoute.getErrorMessage().isEmpty());
    }
}

