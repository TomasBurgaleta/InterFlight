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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class InterconnectionsRestController {

    @Autowired
    private InterconnectionBiz interconnectionBiz;

    @Autowired
    private RoutesBiz routesBiz;

    @Autowired
    private InterconnectionsValidator interconnectionsValidator;

    private static final Logger LOG = LoggerFactory.getLogger(InterconnectionsRestController.class);

    @RequestMapping("/")
    public String welcome() {
        return "Example: http://localhost:8080/microservice/interconnections?departure=BRS&arrival=PMI&departureDateTime=2018-03-30T00:10&arrivalDateTime=2018-03-31T23:55";
    }



    @RequestMapping("/interconnections")
    public List<InterconnectionsResult> message(@RequestParam("departure") String departure,
                                                @RequestParam("arrival") String arrival,
                                                @RequestParam("departureDateTime")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureDateTime,
                                                @RequestParam("arrivalDateTime")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrivalDateTime) throws Exception {

        Interconnections interconnections =  new Interconnections(departure, arrival, departureDateTime, arrivalDateTime);
        //InterconnectionFactory.createInterconnections(departure, arrival, departureDateTime, arrivalDateTime);
        checkRequestBean(interconnections);
        List<InterconnectionsResult> interconnectionsResultList = interconnectionBiz.getInterconnectionsByRoutes(interconnections);
        return interconnectionsResultList;

    }

//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        binder.addValidators(interconnectionsValidator);
//    }

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

    private void checkRequestBean(Interconnections interconnections) throws InterconnectionException{
        if(!routesBiz.isValidDeparture(interconnections.getDeparture())) {
            throw new InterconnectionException("Invalid Departure");
        }
        if(!routesBiz.isValidArrival(interconnections.getArrival())){
            throw new InterconnectionException("Invalid Arrival");
        }
        if (interconnections.getDepartureDateTime().isBefore(LocalDateTime.now()) || interconnections.getArrivalDateTime().isBefore(LocalDateTime.now())) {
            throw new InterconnectionException("Date before today");
        }
        if (interconnections.getDepartureDateTime().isAfter(interconnections.getArrivalDateTime())) {
            throw new InterconnectionException("Departure time is after Arrival time");
        }
    }

    public void setInterconnectionBiz(InterconnectionBiz interconnectionBiz) {
        this.interconnectionBiz = interconnectionBiz;
    }
}
