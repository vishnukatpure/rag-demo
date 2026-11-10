package com.example.rag_pipeline.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.rag_pipeline.model.Authorities;
import com.example.rag_pipeline.model.User;

public interface AuthoritiesRepository extends CrudRepository<Authorities, Long> {

	List<Authorities> findByUsername(User user);

}
