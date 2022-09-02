package com.mindbowser.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.mindbowser.config.jwt.JwtResponse;
import com.mindbowser.config.jwt.JwtUtils;
import com.mindbowser.constant.ExceptionConstant;
import com.mindbowser.constant.StaticKey;
import com.mindbowser.entity.Manager;
import com.mindbowser.entity.Role;
import com.mindbowser.exception.CustomException;
import com.mindbowser.model.ManagerModel;
import com.mindbowser.model.RoleModel;
import com.mindbowser.repo.ManagerRepo;
import com.mindbowser.repo.RoleRepository;
import com.mindbowser.service.ManagerService;
import com.mindbowser.util.CustomDozerHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagerServiceImpl implements ManagerService {

	private Mapper mapper = new DozerBeanMapper();

	@Autowired
	private ManagerRepo userRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private Environment environment;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@Override
	public Manager signUpManager(ManagerModel model) throws CustomException {
		checkSignUpUserRole(model);
		checkSignUpManagerRole(model);
		if (model.getPassword() != null) {
			model.setPassword(encoder.encode(model.getPassword()));
		}
		return userRepository.save(mapper.map(model, Manager.class));

	}

	/**
	 * Check if user with given manager already exists on sign up.
	 *
	 * @param userModel
	 * @throws CustomException
	 */
	private void checkSignUpUserRole(ManagerModel model) throws CustomException {
		Optional<Manager> manager = userRepository.findByEmailAndIsDeleted(model.getEmail(), false);
		if (manager.isPresent()) {
			throw new CustomException(environment.getProperty(ExceptionConstant.USER_ALREADY_EXISTS));
		}

	}

	@Override
	public Object login(ManagerModel model) throws CustomException {
		validateLoginObject(model);
		Manager manager = userRepository.findByEmailAndIsDeleted(model.getEmail(), false)
				.orElseThrow(() -> new CustomException(environment.getProperty(ExceptionConstant.EXC_USER_NOT_FOUND)));
		try {
			if (null != manager) {
				checkUserPassword(model, manager);
				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(model.getEmail(), model.getPassword()));

				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = jwtUtils.generateJwtToken(authentication);

				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList());

				return new JwtResponse(jwt, StaticKey.BEARER, userDetails.getId(), userDetails.getUsername(), roles);
			}
		} catch (HttpClientErrorException ex) {
			if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
				throw new CustomException(environment.getProperty(ExceptionConstant.EXC_INVALID_CREDENTIALS));
			}

			else {
				throw new CustomException(environment.getProperty(ExceptionConstant.EXC_SOMETHING_WENT_WRONG));

			}
		}
		return null;
	}

	private void checkUserPassword(ManagerModel managerModel, Manager manager) {
		if (!new BCryptPasswordEncoder().matches(managerModel.getPassword(), manager.getPassword())) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Check login user model.
	 * 
	 * @author Mindbowser | suraj.jaiswal@mindbowser.com
	 * @throws CustomException
	 * @since Aug 29, 2020
	 * 
	 */
	public void validateLoginObject(ManagerModel userModel) throws CustomException {
		if (userModel.getEmail() == null || (userModel.getPassword() == null)) {
			throwEmptyFieldError();
		}

	}

	/**
	 * Common method to throw field empty error.
	 * 
	 * @author Mindbowser | suraj.jaiswal@mindbowser.com
	 * @since Aug 29, 2020
	 * @throws CustomException
	 */
	public void throwEmptyFieldError() throws CustomException {
		log.error(environment.getProperty(ExceptionConstant.EXC_MISSING_PARAMETERS));
		throw new CustomException(environment.getProperty(ExceptionConstant.EXC_MISSING_PARAMETERS));
	}

	/**
	 * Check if user with given role already exists on sign up.
	 *
	 * @param userModel
	 * @throws CustomException
	 */
	private void checkSignUpManagerRole(ManagerModel model) throws CustomException {

		Set<Role> roles = roleRepository.findByName(model.getRoles().iterator().next().getName());
		if (roles.isEmpty()) {
			throw new CustomException(environment.getProperty(ExceptionConstant.ROLE_NOT_FOUND), HttpStatus.NOT_FOUND);
		}

		model.setRoles(CustomDozerHelper.setMap(mapper, roles, RoleModel.class));
	}

}
