
package com.ryanair.api.model.route;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route  implements Serializable {
    private String airportFrom;
    private String airportTo;
    private String connectingAirport;
    private boolean newRoute;
    private boolean seasonalRoute;
    private String operator;
    private String group;

    public Route() {
    }

    public String getAirportFrom() {
        return this.airportFrom;
    }

    public void setAirportFrom(String airportFrom) {
        this.airportFrom = airportFrom;
    }

    public String getAirportTo() {
        return this.airportTo;
    }

    public void setAirportTo(String airportTo) {
        this.airportTo = airportTo;
    }

    public String getConnectingAirport() {
        return this.connectingAirport;
    }

    public void setConnectingAirport(String connectingAirport) {
        this.connectingAirport = connectingAirport;
    }

    public boolean isNewRoute() {
        return this.newRoute;
    }

    public void setNewRoute(boolean newRoute) {
        this.newRoute = newRoute;
    }

    public boolean isSeasonalRoute() {
        return this.seasonalRoute;
    }

    public void setSeasonalRoute(boolean seasonalRoute) {
        this.seasonalRoute = seasonalRoute;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getRouteKey() {
        return airportFrom + "-" + airportTo;
    }
}
