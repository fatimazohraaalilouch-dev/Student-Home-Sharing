package ma.shs.api.common;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

/**
 * Identifiant de l'utilisateur connecté. Le filtre JWT place l'UUID dans le « subject » du jeton,
 * donc dans {@link Authentication#getName()} : cette méthode accepte un principal String ou UUID.
 */
public final class CurrentUser {

    private CurrentUser() {
    }

    public static UUID id(Authentication authentication) {
        try {
            return UUID.fromString(authentication.getName());
        } catch (RuntimeException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "SESSION_EXPIRED", "Session invalide");
        }
    }
}
