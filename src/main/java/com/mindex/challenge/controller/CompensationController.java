package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

     /**
     * Createing a new Compensation record.
     *
     * @param compensation The Compensation object to be created.
     * @return The Compensation object created.
     */

    @PostMapping("/compensation")
    public Compensation create(@RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for [{}]", compensation);

        return compensationService.create(compensation);
    }
    /**
     * Gets the Compensation based off the employeeID
     * 
     * @param id The employee id
     * @return Compensation record based off id
     */
    @GetMapping("/compensation/{id}")
    public Compensation read(@PathVariable String id) {
        LOG.debug("Received compensation read request for employeeId [{}]", id);

        return compensationService.findByEmployeeId(id);
    }
}
