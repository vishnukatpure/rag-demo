package com.example.rag_pipeline.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rag_pipeline.model.Authorities;
import com.example.rag_pipeline.model.User;
import com.example.rag_pipeline.repository.AuthoritiesRepository;

@Service
public class AuthoritiesService {

	final AuthoritiesRepository authoritiesRepository;

	AuthoritiesService(AuthoritiesRepository authoritiesRepository) {
		this.authoritiesRepository = authoritiesRepository;
	}

	@Transactional
	public List<Authorities> getAllAuthoritiess() {
		return (List<Authorities>) authoritiesRepository.findAll();
	}

	@Transactional
	public Authorities getById(Long id) {
		return authoritiesRepository.findById(id).get();
	}

	@Transactional
	public void deleteAuthorities(Long authoritiesId) {
		authoritiesRepository.deleteById(authoritiesId);
	}

	@Transactional
	public boolean addAuthorities(Authorities authorities) {
		return authoritiesRepository.save(authorities) != null;
	}

	@Transactional
	public boolean updateAuthorities(Authorities authorities) {
		return authoritiesRepository.save(authorities) != null;
	}

	public List<Authorities> findByUserName(User user) {
		return (List<Authorities>) authoritiesRepository.findByUsername(user);
	}
}
