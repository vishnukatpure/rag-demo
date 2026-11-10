package com.example.rag_pipeline.utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

	private static Logger logger = LogManager.getLogger(JwtUtils.class);

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.expiration-seconds:300}")
	private int jwtExpirationSecond;

	private int jwtExpirationMs() {
		return jwtExpirationSecond * 1000;
	}

	public JsonObject generateJwtToken(Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		Date expirationTime = new Date((new Date()).getTime() + jwtExpirationMs());
		List<String> roles = new ArrayList<String>();
		authentication.getAuthorities().stream().forEach(e -> roles.add(e.getAuthority()));
		JwtBuilder builder = Jwts.builder().claim("role", roles).subject((userPrincipal.getUsername()))
				.issuedAt(new Date()).expiration(expirationTime).signWith(key());
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("token", builder.compact());
		jsonObject.addProperty("token-expiry", expirationTime.toString());
		jsonObject.addProperty("token-type", "Bearer ");
		return jsonObject;
	}

	private SecretKey key() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().verifyWith(key()).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
