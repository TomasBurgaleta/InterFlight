package com.ryanair.api.store.rest.consumer;

import com.ryanair.api.model.ResultRestStore;
import com.ryanair.api.model.route.Route;
import com.ryanair.api.model.schedule.ScheduleParameters;
import com.ryanair.api.model.schedule.Schedule;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class SchedulesStoreImpl implements SchedulesStore{

    private static final Logger LOG = LoggerFactory.getLogger(RoutesStoreImpl.class);

    @Autowired
    private RestTemplate appRestClient;

    @Override
    @Cacheable(cacheNames="schedule", key="#scheduleParameters.departure+#scheduleParameters.arrival+#scheduleParameters.month+#scheduleParameters.year")
    public ResultRestStore<Schedule> getSchedules(ScheduleParameters scheduleParameters) {
        ResultRestStore resultRoute = new ResultRestStore<Route>();
        String URL = createScheduleURL(scheduleParameters);
        try {
            LOG.info("REST call to " + URL);
            ResponseEntity<Schedule> routeResponse = appRestClient.exchange(URL, HttpMethod.GET, (HttpEntity)null, new ParameterizedTypeReference<Schedule>() {});
            if (HttpStatus.OK.equals(routeResponse.getStatusCode())) {
                resultRoute.setResult(routeResponse.getBody());
            } else {
                String errorStatus = "Response status not OK, is " + routeResponse.getStatusCode().value() +  " : " + routeResponse.getStatusCode().getReasonPhrase();
                resultRoute.setErrorMessage(errorStatus);
                LOG.error(errorStatus);
            }
        } catch (RestClientException e) {
            LOG.error("error getting REST call to URL " + URL);
            LOG.error(e.getMessage());
            resultRoute.setErrorMessage(e.getMessage());
        }
        return resultRoute;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.appRestClient = appRestClient;
    }

    private String createScheduleURL(ScheduleParameters scheduleParameters) {
        String template = "https://api.ryanair.com/timetable/3/schedules/${departure}/${arrival}/years/${year}/months/${month}";
        Map<String, String> data = new HashMap<String, String>();
        data.put("departure", scheduleParameters.getDeparture());
        data.put("arrival", scheduleParameters.getArrival());
        data.put("year", "" + scheduleParameters.getYear());
        data.put("month", "" + scheduleParameters.getMonth());
        String formattedString = StrSubstitutor.replace(template, data);
        return formattedString;
    }


}
