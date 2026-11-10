package com.example.rag_pipeline.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.rag_pipeline.model.User;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

	@Bean
	AuditorAware<Long> auditorProvider() {

		return () -> {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null || !authentication.isAuthenticated()
					|| authentication instanceof AnonymousAuthenticationToken) {

				return Optional.of(0L); // SYSTEM
			}

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			if (userDetails != null && userDetails instanceof User) {
				User user = (User) userDetails;
				return Optional.of(user.getId());
			}
			return Optional.empty();
		};
	}
}
