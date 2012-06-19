package com.google.code.jee.utils.xml;

import java.util.List;

public class Company {
    private List<Employee> employees;
    private String name;
    private int incomes;
    
    public Company(List<Employee> employees, String name, int incomes) {
        super();
        this.employees = employees;
        this.name = name;
        this.incomes = incomes;
    }
    
    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIncomes() {
        return incomes;
    }
    public void setIncomes(int incomes) {
        this.incomes = incomes;
    }
    
    @Override
    public String toString() {
        return "Company [employees=" + employees + ", name=" + name + ", incomes=" + incomes + "]";
    }
}
