package com.ryanair.api.service.rest.producer;

import com.google.common.base.Joiner;
import com.ryanair.api.biz.InterconnectionException;
import com.ryanair.api.biz.RoutesBiz;
import com.ryanair.api.model.interconnections.InterconnectionsResult;
import com.ryanair.api.model.interconnections.Interconnections;
import com.ryanair.api.model.interconnections.InterconnectionsRequest;
import com.ryanair.api.biz.InterconnectionBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class InterconnectionsRestController {

    @Autowired
    private InterconnectionBiz interconnectionBiz;

    @Autowired
    private RoutesBiz routesBiz;

    private static final Logger LOG = LoggerFactory.getLogger(InterconnectionsRestController.class);

    @RequestMapping("/")
    public String welcome() {
        return "Example: http://localhost:8080/microservice/interconnections?departure=BRS&arrival=PMI&departureDateTime=2018-03-30T00:10&arrivalDateTime=2018-03-31T23:55";
    }


    @RequestMapping("/interconnections")
    public List<InterconnectionsResult> message(@RequestParam("departure") String departure, @RequestParam("arrival") String arrival,
                                                @RequestParam("departureDateTime") String departureDateTime, @RequestParam("arrivalDateTime") String arrivalDateTime) throws Exception {

        InterconnectionsRequest interconnectionsRequest = InterconnectionFactory.createRequestBean(departure, arrival, departureDateTime, arrivalDateTime);
        checkRequestBean(interconnectionsRequest);
        Interconnections interconnections = InterconnectionFactory.createInterconnections(interconnectionsRequest);
        List<InterconnectionsResult> interconnectionsResultList = interconnectionBiz.getInterconnectionsByRoutes(interconnections);
        return interconnectionsResultList;

    }

    @ExceptionHandler(InterconnectionException.class)
    public String  handleInterconnectionException(HttpServletRequest request, InterconnectionException ex){
        LOG.error(ex.getMessage());
        LOG.error("Bad URL parameters: " + request.getRequestURL());
        StringBuilder sb = new  StringBuilder();
        sb.append("Error in request </br>");
        sb.append(ex.getMessage());
        sb.append( "</br>");
        //String mapToString = Joiner.on("</br>").withKeyValueSeparator(" -> ").join(request.getParameterMap());
        return sb.toString();

    }

    private void checkRequestBean(InterconnectionsRequest interconnectionsRequest) throws InterconnectionException{
        if(!routesBiz.isValidDeparture(interconnectionsRequest.getDeparture())) {
            throw new InterconnectionException("Invalid Departure");
        }
        if(!routesBiz.isValidArrival(interconnectionsRequest.getArrival())){
            throw new InterconnectionException("Invalid Arrival");
        }
    }

    public void setInterconnectionBiz(InterconnectionBiz interconnectionBiz) {
        this.interconnectionBiz = interconnectionBiz;
    }
}
