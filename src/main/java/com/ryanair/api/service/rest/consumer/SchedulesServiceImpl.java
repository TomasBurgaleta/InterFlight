package com.ryanair.api.service.rest.consumer;

import com.ryanair.api.model.ResultRestStore;
import com.ryanair.api.model.Schedule.*;
import com.ryanair.api.model.Section;
import com.ryanair.api.store.rest.consumer.SchedulesStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class SchedulesServiceImpl implements SchedulesService {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulesServiceImpl.class);

    @Autowired
    private SchedulesStore schedulesStore;


    @Override
    @Async
    public CompletableFuture<List<FlightResult>> getFlightsBySection(Section section, FlightRoute flightRoute) throws InterruptedException {
        LOG.info("FlightResult to " + section.getRoute());
        List<FlightResult> validFlights = new ArrayList<FlightResult>();
        List<ScheduleParameters> scheduleParametersList = ScheduleFactory.createScheduleParameters(section, flightRoute);
        List<ScheduleResponse> scheduleResponseList = getSchedules(scheduleParametersList);
        for (ScheduleResponse scheduleResponse : scheduleResponseList) {
            List<Day> dayList = getFilterDaysFromSchedule(scheduleResponse, flightRoute.getDepartureDateTime().toLocalDate(), flightRoute.getArrivalDateTime().toLocalDate());
            List<FlightResult> flightResultList = createFlightResultList(dayList, scheduleResponse);
            if(flightResultList != null && !flightResultList.isEmpty()) {
                validFlights.addAll(flightResultList);
            }
        }
        return CompletableFuture.completedFuture(validFlights);
    }

    private List<Day> getFilterDaysFromSchedule(ScheduleResponse scheduleResponse, LocalDate departureDate, LocalDate arrivalDate) {
        Schedule schedule = scheduleResponse.getSchedule();
        List<Day> dayList = schedule.getDays();
        int scheduleMonth = scheduleResponse.getScheduleParameters().getMonth();
        int scheduleYear = scheduleResponse.getScheduleParameters().getYear();
        if (departureDate.getYear() == scheduleYear && departureDate.getMonthValue() == scheduleMonth) {
            return dayList.stream().filter(u -> u.getDay() >= departureDate.getDayOfMonth()).collect(Collectors.toList());
        } else if (arrivalDate.getYear() == scheduleYear && arrivalDate.getMonthValue() == scheduleMonth) {
            return dayList.stream().filter(u -> u.getDay() <= arrivalDate.getDayOfMonth()).collect(Collectors.toList());
        }
        return dayList;
    }


    private List<FlightResult> createFlightResultList(List<Day> dayList, ScheduleResponse scheduleResponse) {
        int year =  scheduleResponse.getScheduleParameters().getYear();
        int month = scheduleResponse.getScheduleParameters().getMonth();
        String departure = scheduleResponse.getScheduleParameters().getDeparture();
        String arrival = scheduleResponse.getScheduleParameters().getArrival();
        List<FlightResult> allFlightResultList = new ArrayList<FlightResult>();
        for (Day day : dayList) {
            List<Flight> flightList = day.getFlights();
            int dayMoth = day.getDay();
            LocalDate localDate = LocalDate.of(year,month,dayMoth);
            List<FlightResult> flightResultList = flightList.stream()
                    .map(obj -> new FlightResult(obj, localDate, departure, arrival)).collect(Collectors.toList());

            allFlightResultList.addAll(flightResultList);
        }
        return allFlightResultList;
    }

    private List<ScheduleResponse> getSchedules(List<ScheduleParameters> scheduleParametersList) {
        List<ScheduleResponse> scheduleResponseList = new ArrayList<ScheduleResponse>();

        for (ScheduleParameters scheduleParameters : scheduleParametersList) {
            ResultRestStore<Schedule> resultRestStore = schedulesStore.getSchedules(scheduleParameters);
            if (resultRestStore.getErrorMessage().isEmpty()) {
                ScheduleResponse scheduleResponse = new ScheduleResponse(scheduleParameters, resultRestStore.getResult());
                scheduleResponseList.add(scheduleResponse);
            }
        }
        return scheduleResponseList;

    }





    public void setSchedulesStore(SchedulesStore schedulesStore) {
        this.schedulesStore = schedulesStore;
    }
}
