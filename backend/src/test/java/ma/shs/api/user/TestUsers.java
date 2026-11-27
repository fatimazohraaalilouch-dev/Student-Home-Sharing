package ma.shs.api.user;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

/**
 * Utilitaire de test : crée un utilisateur en base et simule la connexion (principal = UUID)
 * comme le filtre JWT : principal = identifiant sous forme de texte, rôle USER.
 */
public final class TestUsers {

    private TestUsers() {
    }

    /** Insère un utilisateur actif avec l'espace TENANT, comme après une inscription. */
    public static UUID create(JdbcTemplate jdbc) {
        UUID id = UUID.randomUUID();
        jdbc.update("INSERT INTO app_user (id, email, password_hash, full_name, phone) "
                        + "VALUES (?, ?, 'hash-factice', 'Salma Test', '0600000000')",
                id, "test-" + id + "@example.com");
        jdbc.update("INSERT INTO user_space (user_id, space) VALUES (?, 'TENANT')", id);
        return id;
    }

    public static RequestPostProcessor loggedAs(UUID userId) {
        return authentication(new UsernamePasswordAuthenticationToken(userId.toString(), null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))));
    }
}
