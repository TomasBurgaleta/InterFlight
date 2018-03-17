package com.ryanair.api.service.rest.consumer;

import com.ryanair.api.model.Schedule.FlightRoute;
import com.ryanair.api.model.Schedule.ScheduleParameters;
import com.ryanair.api.model.Section;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleFactory {


    protected static List<ScheduleParameters> createScheduleParameters(Section section, FlightRoute flightRoute) {
        List<ScheduleParameters> scheduleParametersList = new ArrayList<ScheduleParameters>();
        LocalDate departureDate = flightRoute.getDepartureDateTime().toLocalDate();
        LocalDate arrivalDate = flightRoute.getArrivalDateTime().toLocalDate();
        int year = departureDate.getYear();
        int month = departureDate.getMonthValue();
        boolean addScheduleParametersFlag = true;
        while (addScheduleParametersFlag) {
            scheduleParametersList.add(new ScheduleParameters(section.getDeparture(), section.getArrival(), year, month));
            if (isSameYearMonth(departureDate, arrivalDate)) {
                addScheduleParametersFlag = false;
            }
            departureDate = departureDate.plusMonths(1);
            year = departureDate.getYear();
            month = departureDate.getMonthValue();
        }
        return scheduleParametersList;
    }

    private static boolean isSameYearMonth(LocalDate departureDateTime, LocalDate arrivalDateTime) {
        if (departureDateTime.getYear() == arrivalDateTime.getYear()
                && departureDateTime.getMonthValue() == arrivalDateTime.getMonthValue()) {
            return true;
        }
        return false;
    }
}
