package com.ryanair.api.biz;

import com.ryanair.api.model.interconnections.InterconnectionsResult;
import com.ryanair.api.model.interconnections.Interconnections;

import java.util.List;

public interface InterconnectionBiz {



    public List<InterconnectionsResult> getInterconnectionsByRoutes(Interconnections interconnections);


}
