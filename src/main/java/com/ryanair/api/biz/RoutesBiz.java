package com.ryanair.api.biz;

import com.ryanair.api.model.Interconnections.Interconnections;
import com.ryanair.api.model.route.Route;

import java.util.Map;
import java.util.Set;

public interface RoutesBiz {

    public Map<String, Route> getRoutesMap();

    public boolean isValidRoute(Interconnections interconnections);

    public Set<String> getScalingPointForInterconnections(Interconnections interconnections);

}
