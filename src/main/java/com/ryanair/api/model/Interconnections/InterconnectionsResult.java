package com.ryanair.api.model.Interconnections;

import java.util.Collections;
import java.util.List;

public class InterconnectionsResult {

    private int stops;
    private List<Leg> legs = Collections.emptyList();

    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }
}
