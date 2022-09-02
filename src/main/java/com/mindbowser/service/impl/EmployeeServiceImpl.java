package com.mindbowser.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mindbowser.constant.ExceptionConstant;
import com.mindbowser.constant.StaticKey;
import com.mindbowser.entity.Employee;
import com.mindbowser.entity.Manager;
import com.mindbowser.exception.CustomException;
import com.mindbowser.model.EmployeeModel;
import com.mindbowser.repo.EmployeeRepo;
import com.mindbowser.repo.ManagerRepo;
import com.mindbowser.service.EmployeeService;
import com.mindbowser.util.CustomDozerHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
	private Mapper mapper = new DozerBeanMapper();

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private ManagerRepo managerRepo;

	@Autowired
	private Environment environment;

	@Override
	public Employee saveEmployee(EmployeeModel employeeModel) throws CustomException {

		checkEmployeeAlredyExists(employeeModel);
		Employee employee = mapper.map(employeeModel, Employee.class);
		getCurrentLoggedUser(employee);
		return employeeRepo.save(employee);

	}

	@Override
	public Employee updateOrDeleteEmployee(EmployeeModel employeeModel) throws CustomException {

		checkEmployeeExistsOrNot(employeeModel);
		Employee employee = mapper.map(employeeModel, Employee.class);
		getCurrentLoggedUser(employee);
		return employeeRepo.save(employee);

	}

	/**
	 * Check Employee exists or not
	 * 
	 * @author Mindbowser | suraj.jaiswal@mindbowser.com
	 */
	private Employee checkEmployeeExistsOrNot(EmployeeModel employeeModel) throws CustomException {

		Employee employee = employeeRepo.findByIdAndIsDeleted(employeeModel.getId(), false)
				.orElseThrow(() -> new CustomException(environment.getProperty(ExceptionConstant.EXC_USER_NOT_FOUND)));
		// if Employee exists then he can not be change email
		if (!employeeModel.getEmail().equalsIgnoreCase(employee.getEmail()) && !employeeModel.isDeleted()) {
			throw new CustomException(environment.getProperty(ExceptionConstant.EMPLOYEE_CAN_NOT_BE_CHANGE_EMAIL));

		}
		return employee;
	}

	@Override
	public Map<String, Object> getEmployeesList(Integer currentPage, Integer totalPerPage) throws CustomException {
		Map<String, Object> data = new HashMap<>();
		Employee emp = new Employee();
		List<Employee> page = null;
		try {
			getCurrentLoggedUser(emp);
			if (null != emp.getManager()) {

				page = employeeRepo.findByManagerAndIsDeleted(emp.getManager().getId(), false, currentPage - 1,
						totalPerPage);

				if (!page.isEmpty()) {
					data.put(StaticKey.EMPLOYEE_LIST, CustomDozerHelper.map(mapper, page, EmployeeModel.class));
					data.put(StaticKey.TOTAL_RECORD,
							employeeRepo.countByIdAndIsDeleted(emp.getManager().getId(), false));
				} else {
					data.put(StaticKey.EMPLOYEE_LIST, null);
					data.put(StaticKey.TOTAL_RECORD, 0);
				}
			} else {
				throw new CustomException(environment.getProperty(ExceptionConstant.CURRENT_LOGGED_USER_NOT_FOUND));

			}
		} catch (Exception e) {
			log.error("context" + e);
			throw new CustomException(environment.getProperty(ExceptionConstant.EXC_SOMETHING_WENT_WRONG));
		}
		return data;
	}

	/**
	 * Get current logged manager
	 * 
	 * @author Mindbowser | suraj.jaiswal@mindbowser.com
	 */
	private Employee getCurrentLoggedUser(Employee employee) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<Manager> manager = managerRepo.findByEmailAndIsDeleted(auth.getName(), false);
		employee.setManager(manager.get());
		return employee;
	}

	/**
	 * Check if user with given Employee already exists on sign up.
	 *
	 * @param userModel
	 * @throws CustomException
	 */
	private void checkEmployeeAlredyExists(EmployeeModel model) throws CustomException {
		Optional<Employee> manager = employeeRepo.findByEmailAndIsDeleted(model.getEmail(), false);
		if (manager.isPresent()) {
			throw new CustomException(environment.getProperty(ExceptionConstant.USER_ALREADY_EXISTS));
		}

	}

}
