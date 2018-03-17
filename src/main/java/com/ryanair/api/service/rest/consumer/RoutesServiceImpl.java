package com.ryanair.api.service.rest.consumer;

import com.ryanair.api.model.ResultRestStore;
import com.ryanair.api.model.route.Route;
import com.ryanair.api.store.rest.consumer.RoutesStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RoutesServiceImpl implements RoutesService {

    private static final Logger LOG = LoggerFactory.getLogger(RoutesServiceImpl.class);

    @Autowired
    private RoutesStore routesStore;

    @Override
    public List<Route> gelAllRoutes()
    {
        List<Route> routeList = Collections.emptyList();
        ResultRestStore<List<Route>> routeResultRestStore = routesStore.gelAllRoutes();
        if (routeResultRestStore.getErrorMessage().isEmpty()) {
            routeList = routeResultRestStore.getResult();
            LOG.info("getting Routes");
        }
        return routeList;
    }

    public void setRoutesStore(RoutesStore routesStore) {
        this.routesStore = routesStore;
    }


}
