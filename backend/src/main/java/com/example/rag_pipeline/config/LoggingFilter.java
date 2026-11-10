package com.example.rag_pipeline.config;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingFilter extends OncePerRequestFilter {

	private static final String HEADER = "X-Correlation-ID";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {

			String correlationId = request.getHeader(HEADER);
			if (correlationId == null || correlationId.isBlank()) {
				correlationId = UUID.randomUUID().toString();
			}
			// If using Spring Security
			String userName = "anonymous";

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null && authentication.isAuthenticated()) {
				userName = authentication.getName();
			}
			MDC.put("userName", userName);
			MDC.put("correlationId", correlationId);

			response.setHeader(HEADER, correlationId);

			filterChain.doFilter(request, response);

		} finally {
			MDC.remove("userName");
			MDC.remove("correlationId");
		}
	}

}
