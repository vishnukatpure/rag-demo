package com.example.rag_pipeline.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.rag_pipeline.model.Roles;

public interface RolesRepository extends CrudRepository<Roles, Long> {

	List<Roles> findByRole(String role);
}
