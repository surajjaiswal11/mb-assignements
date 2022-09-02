package com.mindbowser.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(exclude = "manager")

@Data
@Entity
public class Employee extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "birth_date")
	private Date birthDate;

	@Column(name = "city")
	private String city;

	@Column(name = "email")
	private String email;

	@Column(name = "address")
	private String address;

	@Column(name = "mobile")
	private String mobile;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "added_by")
	private Manager manager;
}
