package com.cognizant.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cognizant.model.ProjectManager;
import com.cognizant.repository.ManagerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ManagerDetailsService implements UserDetailsService {

	@Autowired
	private ManagerRepository repository;

	@Override
	public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
		log.debug("Start : {}", "loadUserByUsername");

		ProjectManager manager = repository.findById(uid).orElse(null);
		log.debug("End : {}", "loadUserByUsername");

		return new User(manager.getUserId(), manager.getPassword(), new ArrayList<>());

	}

}
