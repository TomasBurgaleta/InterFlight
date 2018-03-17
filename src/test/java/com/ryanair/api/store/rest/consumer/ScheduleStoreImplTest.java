package com.ryanair.api.store.rest.consumer;

import com.ryanair.api.model.ResultRestStore;
import com.ryanair.api.model.Schedule.ScheduleParameters;
import com.ryanair.api.model.Schedule.Schedule;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;


@Test
public class ScheduleStoreImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SchedulesStoreImpl schedulesStore = new SchedulesStoreImpl();




    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Schedule schedule = new Schedule();
        ResponseEntity<Schedule> routeResponse;
        routeResponse = new ResponseEntity(schedule, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), eq(new ParameterizedTypeReference<Schedule>() {}))).thenReturn(routeResponse);
        // mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void readFlightRoutesTest() {
        ScheduleParameters scheduleParameters = new ScheduleParameters("MAD", "BCN", 2018, 5);
        ResultRestStore<Schedule> resultRoute = schedulesStore.getSchedules(scheduleParameters);
        Schedule schedule = resultRoute.getResult();
        assertNotNull(schedule);
        //assertEquals(1, routeList.size());
    }
}

