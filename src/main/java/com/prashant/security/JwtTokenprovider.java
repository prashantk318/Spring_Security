package com.prashant.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenprovider {

	
	@Value("${app.jwt-secret}")
	private String jwtSecret;
	
	@Value("${app.jwt-expiration-millisecons}")
	private int jwtExpirationInMis;
	
	
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expirationDate = new Date(currentDate.getTime()+jwtExpirationInMis);
		String token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).
				setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
		return token;
	}
	
	//get user from token
	public String getUsernameFromJwt(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
	//validate jwtToken
	public boolean validateToken(String token){
	try {	
		Jwts.parser().setSigningKey(jwtSecret).parse(token);
		return true;
	}catch(SignatureException e) {
		throw new RuntimeException("invalid Jwt Signature");
	}catch(MalformedJwtException e) {
		throw new RuntimeException("invalid Jwt token");
	}catch(ExpiredJwtException e) {
		throw new RuntimeException("Jwt token expired");
	}catch(UnsupportedJwtException e) {
		throw new RuntimeException("unsupported Jwt token");
	}catch(IllegalArgumentException e) {
		throw new RuntimeException("Jwt claims string is empty");
	}
	}
}
