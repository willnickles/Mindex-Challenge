package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.mindex.challenge.exception.CompensationAlreadyExistsException;


import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompensationServiceImplTest {

    @Autowired
    private CompensationService compensationService;

    @Test
    public void testCreateReadCompensation_HappyPath() {
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        testCompensation.setSalary(120000);
        testCompensation.setEffectiveDate(LocalDate.now());

        // Create
        Compensation createdCompensation = compensationService.create(testCompensation);
        assertNotNull(createdCompensation);
        assertEquals(testCompensation.getEmployeeId(), createdCompensation.getEmployeeId());

        // Read
        Compensation readCompensation = compensationService.findByEmployeeId(testCompensation.getEmployeeId());
        assertNotNull(readCompensation);
        assertEquals(testCompensation.getSalary(), readCompensation.getSalary(), 0);
        assertEquals(testCompensation.getEffectiveDate(), readCompensation.getEffectiveDate());
    }
    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompensation_NullEmployeeId() {
        // Attempt to create a Compensation with null employeeId
        Compensation invalidCompensation = new Compensation();
        invalidCompensation.setEmployeeId(null);
        invalidCompensation.setSalary(100000);
        invalidCompensation.setEffectiveDate(LocalDate.now());

        compensationService.create(invalidCompensation);
    }

    @Test(expected = CompensationAlreadyExistsException.class)
    public void testCreateCompensation_AlreadyExists() {
        // Create initial Compensation
        Compensation firstCompensation = new Compensation();
        firstCompensation.setEmployeeId("existing-employee-id");
        firstCompensation.setSalary(90000);
        firstCompensation.setEffectiveDate(LocalDate.now());
        compensationService.create(firstCompensation);

        // Attempt to create another Compensation with the same employeeId
        Compensation duplicateCompensation = new Compensation();
        duplicateCompensation.setEmployeeId("existing-employee-id");
        duplicateCompensation.setSalary(95000);
        duplicateCompensation.setEffectiveDate(LocalDate.now().plusDays(1));

        compensationService.create(duplicateCompensation); // This should throw an exception
    }
}