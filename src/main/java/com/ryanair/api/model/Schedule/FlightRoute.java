package com.ryanair.api.model.Schedule;



import com.ryanair.api.model.Section;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FlightRoute {

    public static final String DIRECT_FLY = "directFly";
    public static final String ONE_SCALE = "oneScale";

    private List<Section> sections;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private String typeRoute;

    public FlightRoute(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.sections = Arrays.asList(new Section(departure,arrival));
        this.typeRoute = DIRECT_FLY;
    }

    public FlightRoute(String departure, String interSection, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.sections = Arrays.asList(new Section(departure,interSection), new Section(interSection,arrival));
        this.typeRoute = ONE_SCALE;
    }

    public List<Section> getSections() {
        return sections;
    }

    public String getTypeRoute() {
        return typeRoute;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }
}
