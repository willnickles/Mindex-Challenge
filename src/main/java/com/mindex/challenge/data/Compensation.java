package com.mindex.challenge.data;

import java.util.Date;

public class Compensation {
    private String employeeId; // Links compensation to a specific employee
    private double salary;
    private Date effectiveDate;

    public Compensation() {
    }

    public Compensation(String employeeId, double salary, Date effectiveDate) {
        this.employeeId = employeeId;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
