package com.example.rabbithell.infrastructure.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

	private final JwtProperties properties;
	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
	}

	public boolean hasCloverInfo(String token) {
		Claims claims = parseClaims(token);


		Object cloverId = claims.get("cloverId");
		Object cloverName = claims.get("cloverName");

		return cloverId instanceof Number && cloverName instanceof String;
	}

	public String createMiniToken(Long userId, String role) {
		Date now = new Date();
		long duration = role.equals("ADMIN")
			? Duration.ofHours(12).toMillis() //어드민의 경우 12시간 사용
			: Duration.ofMinutes(5).toMillis(); // USER의 경우 5분 사용 가능

		Date expiry = new Date(now.getTime() + duration);

		return Jwts.builder()
			.setSubject(String.valueOf(userId))
			.claim("role", role)
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public String generateAccessToken(String userId, String role, Long cloverId, String cloverName) {
		return Jwts.builder()
			.setSubject(userId)
			.claim("role", role)
			.claim("cloverId", cloverId)
			.claim("cloverName", cloverName)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + getAccessTokenExpireMillis()))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public String generateRefreshToken(String userId) {
		return Jwts.builder()
			.setSubject(userId)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + getRefreshTokenExpireMillis()))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public String extractSubject(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	public String extractRole(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("role", String.class);
	}

	public Long extractCloverId(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("cloverId", Long.class);
	}

	public String extractCloverName(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("cloverName", String.class);
	}

	private long getAccessTokenExpireMillis() {
		return 1000 * 60 * properties.getToken().getAccess().getMinute();
	}

	private long getRefreshTokenExpireMillis() {
		return 1000 * 60 * properties.getToken().getRefresh().getMinute();
	}

	public Claims parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	//채팅관련
	public String getUsernameFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	public Long getUserIdFromToken(String token) {
		return Long.valueOf(
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject()
		);
	}


}
