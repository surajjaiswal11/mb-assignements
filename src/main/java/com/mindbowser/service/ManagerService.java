package com.mindbowser.service;

import com.mindbowser.entity.Manager;
import com.mindbowser.exception.CustomException;
import com.mindbowser.model.ManagerModel;

public interface ManagerService {

	public Manager signUpManager(ManagerModel username) throws CustomException;

	public Object login(ManagerModel model) throws CustomException;

}
