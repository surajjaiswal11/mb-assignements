package com.mindbowser.service;

import java.util.Map;

import com.mindbowser.entity.Employee;
import com.mindbowser.exception.CustomException;
import com.mindbowser.model.EmployeeModel;

public interface EmployeeService {

	public Employee saveEmployee(EmployeeModel employeeModel) throws CustomException;

	public Employee updateOrDeleteEmployee(EmployeeModel employeeModel) throws CustomException;

	public Map<String, Object> getEmployeesList(Integer currentPage, Integer totalPerPage) throws CustomException;

}
