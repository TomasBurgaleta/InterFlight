package com.ryanair.api.store.rest.consumer;

import com.ryanair.api.model.ResultRestStore;
import com.ryanair.api.model.route.Route;
import java.util.List;

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

@Component
public class RoutesStoreImpl implements RoutesStore {
    private final String URL = "https://api.ryanair.com/core/3/routes";
    private static final Logger LOG = LoggerFactory.getLogger(RoutesStoreImpl.class);

    @Autowired
    private RestTemplate appRestClient;

    @Override
    @Cacheable(cacheNames="route")
    public ResultRestStore<List<Route>> gelAllRoutes() {
        ResultRestStore resultRoute = new ResultRestStore<Route>();

        try {
            LOG.info("REST call to " + URL);
            ResponseEntity<List<Route>> routeResponse = appRestClient.exchange(URL, HttpMethod.GET, (HttpEntity)null, new ParameterizedTypeReference<List<Route>>() {});
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

    public void setRestTemplate(RestTemplate appRestClient) {
        this.appRestClient = appRestClient;
    }

}
