package com.example.rag_pipeline.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private final AuthTokenFilter authTokenFilter;

	public SecurityConfig(AuthTokenFilter authTokenFilter) {
		this.authTokenFilter = authTokenFilter;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth

				.requestMatchers("/non-secured/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
						"/actuator/health", "/actuator/info")
				.permitAll()

				.requestMatchers("/actuator/**").hasAnyRole("ADMIN")

				.anyRequest().hasAnyRole("USER", "ADMIN"));
		// Don't store the user's authentication in an HTTP session. Authenticate each
		// request using the JWT.
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.csrf(csrf -> csrf.disable()).httpBasic(Customizer.withDefaults())
				.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
