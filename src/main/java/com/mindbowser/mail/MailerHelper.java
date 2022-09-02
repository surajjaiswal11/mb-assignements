package com.mindbowser.mail;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindbowser.constant.ExceptionConstant;
import com.mindbowser.constant.SendgridConstant;
import com.mindbowser.constant.StaticKey;
import com.mindbowser.entity.Employee;
import com.mindbowser.exception.CustomException;
import com.mindbowser.model.ManagerModel;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MailerHelper {

	@Autowired
	private Environment environment;

	/**
	 * Create new thread executor and send email.
	 *
	 * @param emailRunnable
	 */
	public void sendEmail(Runnable emailRunnable) {
		ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
		emailExecutor.execute(emailRunnable);
		emailExecutor.shutdown();
	}

	public void sendConfirmationEmail(ManagerModel model) {
		Email from = new Email(environment.getProperty(SendgridConstant.SENDGRID_EMAIL));

		DynamicTemplatePersonalization personalization = new DynamicTemplatePersonalization();
		personalization.addDynamicTemplateData(StaticKey.FIRST_NAME, model.getFirstName());

		personalization.addTo(new Email(model.getEmail()));
		Mail mail = new Mail();

		mail.setFrom(from);
		mail.addPersonalization(personalization);
		mail.setTemplateId(environment.getProperty(SendgridConstant.SUCCESSFULL_MANAGER_REGISTER_TEMPLATE_ID));
		try {
			setContentForMail(mail);
		} catch (CustomException e) {

			log.error("context==" + e.getMessage());
		}
	}

	public void sendEmployeeListEmail(List<Employee> employee, String email) {

		Email from = new Email(environment.getProperty(SendgridConstant.SENDGRID_EMAIL));

		DynamicTemplatePersonalizationListOfEmployee personalization = new DynamicTemplatePersonalizationListOfEmployee();
		personalization.addDynamicListTemplateData(StaticKey.EMPLOYEE, employee);
		personalization.addTo(new Email(email));
		Mail mail = new Mail();
		mail.setFrom(from);
		mail.addPersonalization(personalization);
		mail.setTemplateId(environment.getProperty(SendgridConstant.LIST_OF_EMPLOYEE_TEMPLATE_ID));
		try {
			setContentForMail(mail);
		} catch (CustomException e) {

			log.error("context==" + e.getMessage());
		}
	}

	/**
	 * Dynamic template personalization method.
	 * 
	 * @author mindbowser | suraj.jaiswal@mindbowser.com
	 * @since 29-Aug-2022
	 */
	public static class DynamicTemplatePersonalization extends Personalization {

		@JsonProperty(value = StaticKey.DYNAMIC_TEMPLATE_DATA)
		private Map<String, String> dynamicTemplateData;

		@JsonProperty(StaticKey.DYNAMIC_TEMPLATE_DATA)
		public Map<String, String> getDynamicTemplateData() {
			if (dynamicTemplateData == null) {
				return Collections.<String, String>emptyMap();
			}
			return dynamicTemplateData;
		}

		public void addDynamicTemplateData(String key, String value) {
			if (dynamicTemplateData == null) {
				dynamicTemplateData = new HashMap<>();
				dynamicTemplateData.put(key, value);
			} else {
				dynamicTemplateData.put(key, value);
			}
		}

	}

	/**
	 * Get Mail Object set request and response object for mail sending
	 *
	 * @author Mindbowser | suraj.jaiswal@mindbowser.com
	 * @throws CustomException
	 * @since Sep 1, 2020
	 */
	private void setContentForMail(Mail mail) throws CustomException {

		SendGrid sendgrid = new SendGrid(environment.getProperty(SendgridConstant.SENDGRID_API_KEY));
		Request request = new Request();
		try {

			request.setMethod(Method.POST);
			request.setEndpoint(SendgridConstant.SENDGRID_ENDPOINT);
			request.setBody(mail.build());
			sendgrid.api(request);

		} catch (IOException ex) {
			log.error(ex.getMessage());
			throw new CustomException(environment.getProperty(ExceptionConstant.MAIL_NOT_SEND));
		}

	}

	/**
	 * Dynamic template personalization method for list of employee.
	 * 
	 * @author mindbowser | suraj.jaiswal@mindbowser.com
	 * @since 29-Aug-2022
	 */
	public static class DynamicTemplatePersonalizationListOfEmployee extends Personalization {

		@JsonProperty(value = StaticKey.DYNAMIC_TEMPLATE_DATA)
		private Map<String, List<Employee>> listTemplateData;

		@JsonProperty(StaticKey.DYNAMIC_TEMPLATE_DATA)
		public Map<String, List<Employee>> getListDynamicTemplateData() {
			if (listTemplateData == null) {
				return Collections.<String, List<Employee>>emptyMap();
			}
			return listTemplateData;
		}

		public void addDynamicListTemplateData(String key, List<Employee> value) {
			if (listTemplateData == null) {
				listTemplateData = new HashMap<>();
				listTemplateData.put(key, value);
			} else {
				listTemplateData.put(key, value);
			}
		}

	}

}
