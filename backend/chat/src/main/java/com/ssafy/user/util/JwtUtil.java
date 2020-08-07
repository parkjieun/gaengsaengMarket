package com.ssafy.user.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;


public class JwtUtil {
	
    private Key key;

	public JwtUtil(String secret) {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}
    public String createToken(String userId) {

        String token = Jwts.builder()
                .signWith(key)
                .claim("userId",userId)
                .compact();
        return token;
    }
	public String getClaims(String jwt) {
		Jws<Claims> jws = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(jwt);
		return jws.getBody().get("userId").toString();
	}
}