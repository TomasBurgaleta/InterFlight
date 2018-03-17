package com.ryanair.api.model.Schedule;

public class ScheduleResponse {

    private ScheduleParameters scheduleParameters;
    private Schedule schedule;

    public ScheduleResponse(ScheduleParameters scheduleParameters, Schedule schedule) {
        this.scheduleParameters = scheduleParameters;
        this.schedule = schedule;
    }

    public ScheduleParameters getScheduleParameters() {
        return scheduleParameters;
    }

    public void setScheduleParameters(ScheduleParameters scheduleParameters) {
        this.scheduleParameters = scheduleParameters;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
