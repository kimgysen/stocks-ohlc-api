package be.kpse.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TokenManager {

	private final String expirationTime;

	private final SecretKey key;

	public TokenManager(String secret, String expirationTime) {
		this.expirationTime = expirationTime;

		this.key = Keys.hmacShaKeyFor(secret.getBytes(UTF_8));
	}

	public String createToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", userDetails.getAuthorities());

		return generateToken(claims, userDetails.getUsername());
	}

	private String generateToken(Map<String, Object> claims, String username) {
		long expirationTimeLong = Long.parseLong(expirationTime); //in seconds
		Instant expirationTs = Timestamp
				.from(Instant.now())
				.toInstant()
				.plusSeconds(expirationTimeLong);

		return Jwts.builder()
				.claims(claims)
				.subject(username)
				.issuedAt(new Date())
				.expiration(Date.from(expirationTs))
				.signWith(key)
				.compact();
	}

	public void validateToken(String token, UserDetails userDetails) {
		String username = getUsernameFromToken(token);
		if (isTokenExpired(token)) {
			throw new AccountExpiredException("ExpiredJwtException");
		} else if (!username.equals(userDetails.getUsername())) {
			throw new BadCredentialsException("Jwt is invalid because user details don't match");
		}
	}

	public String getUsernameFromToken(String token) {
		return getAllClaimsFromToken(token)
				.getSubject();
	}

	public boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts
				.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	private Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

}
