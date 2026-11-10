package com.example.rag_pipeline.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.rag_pipeline.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	List<User> findByUsername(String username);

	boolean existsByUsername(String username);
}
