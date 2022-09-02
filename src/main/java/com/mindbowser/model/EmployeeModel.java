package com.mindbowser.model;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindbowser.constant.StaticKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeModel extends BaseModel {

	private static final long serialVersionUID = 1L;

	@Pattern(regexp = StaticKey.REG_EXPRESSION, message = StaticKey.INVALID_INPUT)
	@NotBlank(message = StaticKey.FIRST_NAME + StaticKey.SHOULD_NOT_BE_BLANK)
	@Size(min = 3, message = StaticKey.FIRST_NAME + StaticKey.SHOULD_BE_AT_LEAST_THREE_CHAR)
	private String firstName;

	@Pattern(regexp = StaticKey.REG_EXPRESSION, message = StaticKey.INVALID_INPUT)
	@NotBlank(message = StaticKey.LAST_NAME + StaticKey.SHOULD_NOT_BE_BLANK)
	@Size(min = 3, message = StaticKey.LAST_NAME + StaticKey.SHOULD_BE_AT_LEAST_THREE_CHAR)
	private String lastName;

	@PastOrPresent
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;

	@NotBlank(message = StaticKey.CITY + StaticKey.SHOULD_NOT_BE_BLANK)
	@Size(min = 3, message = StaticKey.CITY + StaticKey.SHOULD_BE_AT_LEAST_THREE_CHAR)
	private String city;

	@NotBlank(message = StaticKey.EMAIL + StaticKey.SHOULD_NOT_BE_BLANK)
	@Email
	private String email;

	@NotBlank(message = StaticKey.ADDRESS + StaticKey.SHOULD_NOT_BE_BLANK)
	@Size(min = 3, message = StaticKey.ADDRESS + StaticKey.SHOULD_BE_AT_LEAST_THREE_CHAR)
	private String address;

	@NotBlank(message = StaticKey.MOBILE_NUMBER_IS_REQUIRED)
	@Size(min = 10, max = 10)
	@Pattern(regexp = "(^$|[0-9]{10})")
	private String mobile;

}
