package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Collections;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = "http://localhost:" + port + "/employee/{id}/reporting-structure";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }
    @Test
    public void testReportingStructure_0Reports() {
        // Create a single employee with no direct reports
        Employee manager = new Employee();
        manager.setFirstName("Alice");
        manager.setLastName("Johnson");
        manager.setDepartment("HR");
        manager.setPosition("Manager");

        // Create the employee
        Employee createdManager = restTemplate.postForEntity(employeeUrl, manager, Employee.class).getBody();

        assertNotNull(createdManager);
        assertNotNull(createdManager.getEmployeeId());

        // Fetch the created employee to check persistence
        Employee fetchedManager = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdManager.getEmployeeId()).getBody();
        assertNotNull(fetchedManager);
        assertEquals(createdManager.getEmployeeId(), fetchedManager.getEmployeeId());

        // Call reporting structure endpoint
        ReportingStructure reportingStructure = restTemplate.getForEntity(
                reportingStructureUrl,
                ReportingStructure.class,
                createdManager.getEmployeeId()).getBody();

        // Validate response
        assertNotNull(reportingStructure);
        assertNotNull(reportingStructure.getEmployee());
        assertEquals(createdManager.getEmployeeId(), reportingStructure.getEmployee().getEmployeeId());

        // Since there are no direct reports, numberOfReports should be 0
        assertEquals(0, reportingStructure.getNumberOfReports());
    }
    @Test
    public void testReportingStructure_OneDirectReport() {
        // Create the direct report employee
        Employee directReport = new Employee();
        directReport.setFirstName("Jane");
        directReport.setLastName("Smith");
        directReport.setDepartment("Engineering");
        directReport.setPosition("Developer");

        // Persist the direct report employee
        Employee createdDirectReport = restTemplate.postForEntity(employeeUrl, directReport, Employee.class).getBody();
        assertNotNull(createdDirectReport);
        assertNotNull(createdDirectReport.getEmployeeId());

        // Create the manager employee and assign the direct report
        Employee manager = new Employee();
        manager.setFirstName("John");
        manager.setLastName("Doe");
        manager.setDepartment("Engineering");
        manager.setPosition("Manager");
        manager.setDirectReports(Collections.singletonList(createdDirectReport));

        // Persist the manager employee
        Employee createdManager = restTemplate.postForEntity(employeeUrl, manager, Employee.class).getBody();
        assertNotNull(createdManager);
        assertNotNull(createdManager.getEmployeeId());

        // Fetch the created manager to check persistence
        Employee fetchedManager = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdManager.getEmployeeId()).getBody();
        assertNotNull(fetchedManager);
        assertEquals(createdManager.getEmployeeId(), fetchedManager.getEmployeeId());

        // Call the reporting structure endpoint
        ReportingStructure reportingStructure = restTemplate.getForEntity(
                reportingStructureUrl,
                ReportingStructure.class,
                createdManager.getEmployeeId()).getBody();

        // Validate the response
        assertNotNull(reportingStructure);
        assertNotNull(reportingStructure.getEmployee());
        assertEquals(createdManager.getEmployeeId(), reportingStructure.getEmployee().getEmployeeId());

        // Since there is one direct report, numberOfReports should be 1
        assertEquals(1, reportingStructure.getNumberOfReports());
    }



    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
