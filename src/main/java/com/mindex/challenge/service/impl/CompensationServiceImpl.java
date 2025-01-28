package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mindex.challenge.exception.CompensationNotFoundException;
import com.mindex.challenge.exception.CompensationAlreadyExistsException;


@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    /**
     * Creates a new Compensation entry in the database.
     * 
     * @param compensation The Compensation object to be created.
     * @return The created Compensation object.
     * @throws IllegalArgumentException If employeeId is null or empty.
     * @throws CompensationAlreadyExistsException If a Compensation already exists for the given employeeId.
     */

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        if (compensation.getEmployeeId() == null || compensation.getEmployeeId().isEmpty()) {
            throw new IllegalArgumentException("EmployeeId is required for creating compensation");
        }
         // Check if compensation already exists
        Compensation existingCompensation = compensationRepository.findByEmployeeId(compensation.getEmployeeId());
        if (existingCompensation != null) {
            throw new CompensationAlreadyExistsException("Compensation already exists for employeeId: "
                    + compensation.getEmployeeId() + ". Please use a PUT endpoint to update it.");
        }
        compensationRepository.save(compensation);

		return compensation;
        
    }
    /**
     * Finds a Compensation entry by employeeId.
     * 
     * @param employeeId The employeeId for which to retrieve the Compensation.
     * @return The Compensation object if found.
     * @throws CompensationNotFoundException If no Compensation exists for the given employeeId.
     */

    @Override
    public Compensation findByEmployeeId(String employeeId) {
        LOG.debug("Reading compensation for employeeId [{}]", employeeId);
        Compensation compensation = compensationRepository.findByEmployeeId(employeeId);
          
          // If no compensation is found, throw an exception
         if (compensation == null) {
            throw new CompensationNotFoundException("Compensation not found for employeeId: " + employeeId);
        }

        return compensation;
    }
}
