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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoutesServiceImpl implements RoutesService {

    private static final Logger LOG = LoggerFactory.getLogger(RoutesServiceImpl.class);


    @Autowired
    private RoutesStore routesStore;

    @Override
    public List<Route> getAllRoutes()
    {
        List<Route> routeList = Collections.emptyList();
        ResultRestStore<List<Route>> routeResultRestStore = routesStore.gelAllRoutes();
        if (routeResultRestStore.getErrorMessage().isEmpty()) {
            routeList = routeResultRestStore.getResult();
            LOG.info("getting Routes");
        }
        return getFilterRouteList(routeList);
    }

    @Override
    public List<Route> getRoutesByOperator(String operator) {
        List<Route> routeList = Collections.emptyList();
        ResultRestStore<List<Route>> routeResultRestStore = routesStore.gelAllRoutes();
        if (routeResultRestStore.getErrorMessage().isEmpty()) {
            routeList = routeResultRestStore.getResult();
            LOG.info("getting Routes");
        }
        return getFilterbyOperator(routeList, operator);
    }

    private List<Route> getFilterbyOperator(List<Route> routeList, String operator)  {
        return routeList.stream().filter(u -> u.getConnectingAirport() == null).filter(u -> u.getOperator().equals(operator)).collect(Collectors.toList());
    }



    private List<Route> getFilterRouteList(List<Route> routeList)  {
        return routeList.stream().filter(u -> u.getConnectingAirport() == null).collect(Collectors.toList());
    }

    public void setRoutesStore(RoutesStore routesStore) {
        this.routesStore = routesStore;
    }


}
