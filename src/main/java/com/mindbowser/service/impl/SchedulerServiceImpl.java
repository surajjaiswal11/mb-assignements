package com.mindbowser.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mindbowser.constant.StaticKey;
import com.mindbowser.entity.Employee;
import com.mindbowser.entity.Manager;
import com.mindbowser.mail.MailerHelper;
import com.mindbowser.repo.EmployeeRepo;
import com.mindbowser.repo.ManagerRepo;

@Service
public class SchedulerServiceImpl {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private ManagerRepo managerRepo;

	@Autowired
	private MailerHelper mailHelper;

	@Scheduled(cron = StaticKey.CRON_EXPRESSION)
	public void schedulerSendEmailToManagers() {
		List<Manager> managers = managerRepo.findAllByIsDeleted(false);
		for (Manager manager : managers) {

			List<Employee> employee = employeeRepo.findByIsDeleted(manager.getId(), false);
			if (!employee.isEmpty()) {
				mailHelper.sendEmail(() -> mailHelper.sendEmployeeListEmail(employee, manager.getEmail()));

			}
		}
	}

}
