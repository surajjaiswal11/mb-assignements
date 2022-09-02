package com.mindbowser.model;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mindbowser.constant.StaticKey;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode(callSuper = false)
@Data
public class ManagerModel extends BaseModel {

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

	@NotBlank(message = StaticKey.COMPANY_NAME + StaticKey.SHOULD_NOT_BE_BLANK)
	@Size(min = 3, message = StaticKey.COMPANY_NAME + StaticKey.SHOULD_BE_AT_LEAST_THREE_CHAR)
	private String companyName;

	@NotBlank(message = StaticKey.EMAIL + StaticKey.SHOULD_NOT_BE_BLANK)
	@Email
	private String email;

	@NotBlank(message = StaticKey.ADDRESS + StaticKey.SHOULD_NOT_BE_BLANK)
	@Size(min = 3, message = StaticKey.ADDRESS + StaticKey.SHOULD_BE_AT_LEAST_THREE_CHAR)
	private String address;

	@NotBlank(message = StaticKey.PASS_IS_MANDATORY)
	private String password;

	private Set<RoleModel> roles;

}
