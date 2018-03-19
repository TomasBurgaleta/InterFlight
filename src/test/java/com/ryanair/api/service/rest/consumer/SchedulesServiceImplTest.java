package com.ryanair.api.service.rest.consumer;

import com.ryanair.api.model.ResultRestStore;
import com.ryanair.api.model.Section;
import com.ryanair.api.model.flightRoute.FlightRoute;
import com.ryanair.api.model.flightRoute.FlightRouteParameters;
import com.ryanair.api.model.schedule.Day;
import com.ryanair.api.model.schedule.Flight;
import com.ryanair.api.model.schedule.Schedule;
import com.ryanair.api.model.schedule.ScheduleParameters;
import com.ryanair.api.store.rest.consumer.SchedulesStore;
import com.ryanair.api.store.rest.consumer.SchedulesStoreImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import sun.swing.BakedArrayList;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

@Test
public class SchedulesServiceImplTest {

    @Mock
    private SchedulesStore schedulesStore;

    @InjectMocks
    private SchedulesServiceImpl schedulesServiceimpl = new SchedulesServiceImpl();

    private String departureIATA = "MAD";
    private String arrivalIATA = "BCN";
    private Section section = new Section();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        section.setDeparture(departureIATA);
        section.setArrival(arrivalIATA);

    }

    @Test
    public void getFlightRouteListOneMonthTest() throws InterruptedException, ExecutionException {
        LocalDateTime departureTime = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime arrivalTime = LocalDateTime.now().withDayOfMonth(1).plusDays(15);
        long totalDays = DAYS.between(departureTime.toLocalDate(), arrivalTime.toLocalDate());
        FlightRouteParameters flightRouteParameters = new FlightRouteParameters(departureIATA, arrivalIATA, departureTime, arrivalTime);
        ScheduleParameters scheduleParametersFirstMonth = new ScheduleParameters(departureIATA, arrivalIATA, departureTime.getYear(), departureTime.getMonthValue());
        ResultRestStore<Schedule> resultRestStoreFirstMonth = createResultRestStore(scheduleParametersFirstMonth, departureTime);
        when(schedulesStore.getSchedules(eq(scheduleParametersFirstMonth))).thenReturn(resultRestStoreFirstMonth);
        CompletableFuture<List<FlightRoute>> future = schedulesServiceimpl.getFlightRouteListBySection(section,flightRouteParameters);
        CompletableFuture.allOf(future).join();
        List<FlightRoute> validFlights = future.get();
        assertTrue(!validFlights.isEmpty());
        assertEquals(totalDays + 1, validFlights.size());
        verify(schedulesStore,times(1)).getSchedules(any(ScheduleParameters.class));
    }

    @Test
    public void getFlightRouteListTwoMonthTest() throws InterruptedException, ExecutionException {

        LocalDateTime departureTime = LocalDateTime.now();
        LocalDateTime arrivalTime = LocalDateTime.now().plusMonths(1);
        long totalDays = DAYS.between(departureTime.toLocalDate(), arrivalTime.toLocalDate());
        FlightRouteParameters flightRouteParameters = new FlightRouteParameters(departureIATA, arrivalIATA, departureTime, arrivalTime);

        ScheduleParameters scheduleParametersFirstMonth = new ScheduleParameters(departureIATA, arrivalIATA, departureTime.getYear(), departureTime.getMonthValue());
        ScheduleParameters scheduleParametersSecondMonth = new ScheduleParameters(departureIATA, arrivalIATA, arrivalTime.getYear(), arrivalTime.getMonthValue());
        ResultRestStore<Schedule> resultRestStoreFirstMonth = createResultRestStore(scheduleParametersFirstMonth, departureTime);
        ResultRestStore<Schedule> resultRestStoreSecondMonth = createResultRestStore(scheduleParametersSecondMonth, arrivalTime);
        when(schedulesStore.getSchedules(eq(scheduleParametersFirstMonth))).thenReturn(resultRestStoreFirstMonth);
        when(schedulesStore.getSchedules(eq(scheduleParametersSecondMonth))).thenReturn(resultRestStoreSecondMonth);
        CompletableFuture<List<FlightRoute>> future = schedulesServiceimpl.getFlightRouteListBySection(section,flightRouteParameters);
        CompletableFuture.allOf(future).join();
        List<FlightRoute> validFlights = future.get();
        assertTrue(!validFlights.isEmpty());
        assertEquals(totalDays + 1, validFlights.size());
        verify(schedulesStore,times(2)).getSchedules(any(ScheduleParameters.class));
    }

    private ResultRestStore<Schedule> createResultRestStore(ScheduleParameters scheduleParameters, LocalDateTime time) {
        ResultRestStore<Schedule> resultRestStore = new ResultRestStore<Schedule>();
        Schedule schedule = createSchedule(time);
        resultRestStore.setResult(schedule);
        return resultRestStore;
    }

    private Schedule createSchedule(LocalDateTime time) {
        Schedule schedule = new Schedule();
        LocalDate timeSchedule = time.toLocalDate().withDayOfMonth(1);
        int month = timeSchedule.getMonthValue();
        int day = 1;
        schedule.setMonth(month);
        List<Day> dayList =  new ArrayList<Day>();
        while(month == time.getMonthValue()) {
            Day scheduleDay = new Day();
            Flight flight = new Flight("1926", "09:45", "13:20");
            scheduleDay.setFlights(Arrays.asList(flight));
            scheduleDay.setDay(day);
            dayList.add(scheduleDay);
            timeSchedule = timeSchedule.plusDays(1);
            month = timeSchedule.getMonthValue();
            day = timeSchedule.getDayOfMonth();
        }
        schedule.setDays(dayList);
        return schedule;
    }



}
