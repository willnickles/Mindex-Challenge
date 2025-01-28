package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.Queue;
import java.util.LinkedList;
import com.mindex.challenge.data.ReportingStructure;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
    @Override
    public ReportingStructure getReportingStructure(String id){
        //Adding a log for reporting purposes
        LOG.debug("Calculating reporting structure for employee with id [{}] using iterative approach", id);

        Employee employee = read(id);
        int numberOfReports = calculateTotalReportsIteratively(employee);

        return new ReportingStructure(employee, numberOfReports);
    }
    // Iterative approach using a queue 
    private int calculateTotalReportsIteratively(Employee employee) {
        if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            return 0;
        }
        int totalReports = 0;
        Queue<Employee> queue = new LinkedList<>();

        // Add all direct reports to the queue
        for (Employee directReport : employee.getDirectReports()) {
            Employee fullDirectReport = employeeRepository.findByEmployeeId(directReport.getEmployeeId());
            queue.add(fullDirectReport);
        }

    // Process the queue iteratively
        while (!queue.isEmpty()) {
            Employee current = queue.poll();
            totalReports++;

            if (current.getDirectReports() != null) {
                for (Employee report : current.getDirectReports()) {
                    Employee fullReport = employeeRepository.findByEmployeeId(report.getEmployeeId());
                    queue.add(fullReport);
                }
            }
        }

        return totalReports;
    }
}
