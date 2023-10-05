package com.acountservice.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.acountservice.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
	
	private static String SECRET_KEY = "amit123";
	
	public static String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		
		return createToken(claims, user.getUserEmail());
	}

	private static String createToken(Map<String, Object> claims, String userName) {


		return Jwts.builder().setClaims(claims).setSubject(userName).
				setIssuedAt(new Date(System.currentTimeMillis())).
				setExpiration(new Date(System.currentTimeMillis()+1000*60*10)).
				signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	
	
	public Boolean validateToken(String token, String userName) {
		final String username = extractUsername(token);
		return (username.equals(userName) && !isTokenExpired(token));
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}



	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

}
