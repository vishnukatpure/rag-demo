package com.example.rag_pipeline.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.rag_pipeline.model.Roles;
import com.example.rag_pipeline.repository.RolesRepository;

@Service
public class RolesService {

	final RolesRepository rolesRepository;

	RolesService(RolesRepository rolesRepository) {
		this.rolesRepository = rolesRepository;
	}

	@Transactional
	public List<Roles> getAllAuthoritiess() {
		return (List<Roles>) rolesRepository.findAll();
	}

	@Transactional
	public Roles getById(Long id) {
		return rolesRepository.findById(id).get();
	}

	@Transactional
	public void deleteAuthorities(Long authoritiesId) {
		rolesRepository.deleteById(authoritiesId);
	}

	@Transactional
	public boolean addAuthorities(Roles roles) {
		return rolesRepository.save(roles) != null;
	}

	@Transactional
	public boolean updateAuthorities(Roles roles) {
		return rolesRepository.save(roles) != null;
	}

	@Transactional
	public Roles findByRole(String role) {
		List<Roles> roles = rolesRepository.findByRole(role);
		return roles.isEmpty() ? null : roles.get(0);
	}
}
