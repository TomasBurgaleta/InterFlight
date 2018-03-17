package com.ryanair.api.service.rest.producer;

import com.ryanair.api.model.Interconnections.InterconnectionsResult;
import com.ryanair.api.model.Interconnections.Interconnections;
import com.ryanair.api.model.Interconnections.InterconnectionsRequest;
import com.ryanair.api.biz.InterconnectionBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
public class InterconnectionsRestController {

    @Autowired
    private InterconnectionBiz interconnectionBiz;

    @RequestMapping("/")
    public String welcome() {
        return "Example: http://localhost:8080/microservice/interconnections?departure=BRS&arrival=PMI&departureDateTime=2018-03-30T00:10&arrivalDateTime=2018-03-31T23:55";
    }



    @RequestMapping("/interconnections")
    public List<InterconnectionsResult> message(@RequestParam("departure") String departure, @RequestParam("arrival") String arrival, @RequestParam("departureDateTime") String departureDateTime, @RequestParam("arrivalDateTime") String arrivalDateTime) {

        InterconnectionsRequest interconnectionsRequest = InterconnectionFactory.createRequestBean(departure, arrival, departureDateTime, arrivalDateTime);
        Interconnections interconnections = InterconnectionFactory.createInterconnections(interconnectionsRequest);
        List<InterconnectionsResult> interconnectionsResultList = interconnectionBiz.getRoutes(interconnections);
        return interconnectionsResultList;

    }





    public void setInterconnectionBiz(InterconnectionBiz interconnectionBiz) {
        this.interconnectionBiz = interconnectionBiz;
    }
}
