package ma.shs.api.user;

import java.util.List;
import java.util.UUID;

/** Schéma User de docs/openapi.yaml. */
public record UserResponse(
        UUID id,
        String email,
        String fullName,
        String phone,
        String avatarUrl,
        String preferredLanguage,
        String activeSpace,
        List<String> spaces) {
}
