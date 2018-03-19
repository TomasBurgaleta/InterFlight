package com.ryanair.api.model.flightRoute;



import com.ryanair.api.model.Section;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FlightRouteParameters {

    public static final String DIRECT_FLY = "directFly";
    public static final String ONE_SCALE = "oneScale";

    private List<Section> sections;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private String typeRoute;

    public FlightRouteParameters(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.sections = Arrays.asList(new Section(departure,arrival));
        this.typeRoute = DIRECT_FLY;
    }

    public FlightRouteParameters(String departure, String interSection, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightRouteParameters that = (FlightRouteParameters) o;
        return Objects.equals(sections, that.sections) &&
                Objects.equals(departureDateTime, that.departureDateTime) &&
                Objects.equals(arrivalDateTime, that.arrivalDateTime) &&
                Objects.equals(typeRoute, that.typeRoute);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sections, departureDateTime, arrivalDateTime, typeRoute);
    }
}
