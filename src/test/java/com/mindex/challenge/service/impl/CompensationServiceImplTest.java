package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

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
        testCompensation.setEffectiveDate(new Date());

        // Create
        Compensation createdCompensation = compensationService.create(testCompensation);
        assertNotNull(createdCompensation);
        assertEquals(testCompensation.getEmployeeId(), createdCompensation.getEmployeeId());

        // Read
        Compensation readCompensation = compensationService.read(testCompensation.getEmployeeId());
        assertNotNull(readCompensation);
        assertEquals(testCompensation.getSalary(), readCompensation.getSalary(), 0);
        assertEquals(testCompensation.getEffectiveDate(), readCompensation.getEffectiveDate());
    }
}
