package com.ryanair.api.store.rest.consumer;

import com.ryanair.api.model.ResultRestStore;

import com.ryanair.api.model.route.Route;

import java.util.List;

public interface RoutesStore {

    ResultRestStore<List<Route>> gelAllRoutes();
}
