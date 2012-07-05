package com.google.code.jee.utils.xml;

public class Employee {
    private String name;
    private String designation;
    private String department;
    private Address address;

    public Employee(String name, String designation, String department, Address address) {
        super();
        this.name = name;
        this.designation = designation;
        this.department = department;
        this.setAddress(address);
    }

    public Employee(String name, String designation, String department) {
        super();
        this.name = name;
        this.designation = designation;
        this.department = department;
    }

    public Employee(String name, String designation, Address address) {
        super();
        this.name = name;
        this.designation = designation;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Name : " + this.name + "\nDesignation : " + this.designation + "\nDepartment : " + this.department;
    }
}
