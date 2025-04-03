package com.loginuser.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private String secretKey="";
	
	public JwtService() {
		try {
		KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk=keyGen.generateKey();
			 secretKey= Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
		}
	}
	public String generateToken(String username) {
		Map<String, Object> claims=new HashMap<>();
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+60*60*1000))
				.and()
				.signWith(getKey())
				.compact();
		//		return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";
	}

	private SecretKey getKey() {

		byte [] keyBytes=Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String extractUserName(String token) {
		// TODO Auto-generated method stub
		return extractClaim(token,Claims::getSubject);
	}
	private <T> T extractClaim(String token, Function<Claims, T>claimsResolver) {
		final Claims claims=extractAllClaims(token);
		
		return claimsResolver.apply(claims);
	}
	private Claims extractAllClaims(String token) {
		// TODO Auto-generated method stub
		return Jwts.parser()
				.verifyWith(getKey())
				.build().parseSignedClaims(token).getPayload ();
	}
	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName=extractUserName(token);
		
		return (userName.equals(userDetails.getUsername())&&!isTokenExpired(token));
	}
	private boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
	}
	private Date extractExpiration(String token) {
		
		return extractClaim(token, Claims::getExpiration);
	}

	
}
