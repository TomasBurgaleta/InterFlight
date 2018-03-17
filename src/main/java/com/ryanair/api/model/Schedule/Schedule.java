package com.ryanair.api.model.Schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule implements Serializable{

    private int month;
    private List<Day> days = Collections.EMPTY_LIST;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
}
