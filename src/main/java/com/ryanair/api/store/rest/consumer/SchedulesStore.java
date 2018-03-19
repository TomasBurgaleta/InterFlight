package com.ryanair.api.store.rest.consumer;

import com.ryanair.api.model.ResultRestStore;
import com.ryanair.api.model.schedule.ScheduleParameters;
import com.ryanair.api.model.schedule.Schedule;

public interface SchedulesStore {

    public ResultRestStore<Schedule> getSchedules(ScheduleParameters scheduleParameters);
}
