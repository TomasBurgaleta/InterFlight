package com.ryanair.api.biz;

import com.ryanair.api.model.Interconnections.InterconnectionsResult;
import com.ryanair.api.model.Interconnections.Interconnections;

import java.util.List;

public interface InterconnectionBiz {



    public List<InterconnectionsResult> getRoutes(Interconnections interconnections);


}
