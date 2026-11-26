package ma.shs.api.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ma.shs.api.auth.AuthDtos.AuthTokens;
import ma.shs.api.auth.AuthDtos.LoginRequest;
import ma.shs.api.auth.AuthDtos.RegisterRequest;
import ma.shs.api.common.ApiException;

@Service
public class AuthService {
	private static final SecureRandom RANDOM = new SecureRandom();

    private final JdbcTemplate jdbc;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final long refreshTtlDays;

    public AuthService(JdbcTemplate jdbc, PasswordEncoder encoder, JwtService jwt,
                       @Value("${app.jwt.refresh-ttl-days}") long refreshTtlDays) {
        this.jdbc = jdbc;
        this.encoder = encoder;
        this.jwt = jwt;
        this.refreshTtlDays = refreshTtlDays;
    }

    @Transactional
    public AuthTokens register(RegisterRequest r) {
        String email = r.email().trim().toLowerCase();
        Integer n = jdbc.queryForObject("SELECT count(*) FROM app_user WHERE email = ?", Integer.class, email);
        if (n != null && n > 0) {
            // VALIDATION_ERROR (400) avec détail par champ, comme dans l'OpenAPI
        	throw ApiException.validation("email", "Adresse e-mail déjà utilisée");
        }
        UUID id = UUID.randomUUID();
        jdbc.update("INSERT INTO app_user (id, email, password_hash, full_name, phone) VALUES (?, ?, ?, ?, ?)",
                id, email, encoder.encode(r.password()), r.fullName(), r.phone());
        return issueTokens(id);
    }

    public AuthTokens login(LoginRequest r) {
        var rows = jdbc.queryForList("SELECT id, password_hash FROM app_user WHERE email = ?",
                r.email().trim().toLowerCase());
        // Même réponse si l'e-mail n'existe pas ou si le mot de passe est faux
        if (rows.isEmpty() || !encoder.matches(r.password(), (String) rows.get(0).get("password_hash"))) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS",
                    "E-mail ou mot de passe incorrect");
        }
        return issueTokens((UUID) rows.get(0).get("id"));
    }

    @Transactional
    public AuthTokens refresh(String refreshToken) {
        var rows = jdbc.queryForList(
                "SELECT id, user_id, expires_at, revoked_at FROM refresh_token WHERE token_hash = ?",
                sha256(refreshToken));
        if (rows.isEmpty()) throw sessionExpired();
        Map<String, Object> row = rows.get(0);
        Timestamp expiresAt = (Timestamp) row.get("expires_at");
        if (row.get("revoked_at") != null || expiresAt.toInstant().isBefore(Instant.now())) {
            throw sessionExpired();
        }
        // Rotation : l'ancien jeton est révoqué, un nouveau couple est émis
        jdbc.update("UPDATE refresh_token SET revoked_at = now() WHERE id = ?", row.get("id"));
        return issueTokens((UUID) row.get("user_id"));
    }

    public void logout(UUID userId) {
        jdbc.update("UPDATE refresh_token SET revoked_at = now() WHERE user_id = ? AND revoked_at IS NULL", userId);
    }

    public void requestPasswordReset(String email) {
        // Réponse 202 identique que l'e-mail existe ou non (on ne révèle rien).
        // Pas encore d'envoi d'e-mail (hors périmètre S1) : ne rien écrire dans les logs
        // (ni e-mail, ni jeton).
    }

    private AuthTokens issueTokens(UUID userId) {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        String refresh = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        Timestamp expires = Timestamp.from(Instant.now().plus(refreshTtlDays, ChronoUnit.DAYS));
        jdbc.update("INSERT INTO refresh_token (user_id, token_hash, expires_at) VALUES (?, ?, ?)",
                userId, sha256(refresh), expires);
        return new AuthTokens(jwt.createAccessToken(userId.toString(), "USER"), refresh);
    }

    private static String sha256(String value) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(d);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static ApiException sessionExpired() {
        return new ApiException(HttpStatus.UNAUTHORIZED, "SESSION_EXPIRED", "Session expirée");
    }
}
