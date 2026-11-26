package ma.shs.api.auth;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private final SecretKey key;
	 private final long accessTtlMin;
	 public JwtService(@Value("${shs.jwt-secret}") String secret, @Value("${app.jwt.access-ttl-minutes}") long accessTtlMin) {
		 this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // 32 caractères minimum
		 this.accessTtlMin = accessTtlMin;
	 }
	 public String createAccessToken(String userId, String role) {
	 Instant now = Instant.now();
		 return Jwts.builder().subject(userId).claim("role", role)
		 .issuedAt(Date.from(now))
		 .expiration(Date.from(now.plus(accessTtlMin, ChronoUnit.MINUTES)))
		 .signWith(key).compact();
	 }
	 public Claims parse(String token) { // lève JwtException si invalide, ExpiredJwtException si expiré
		 return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	 }
}
