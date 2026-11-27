package ma.shs.api.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Schéma UserUpdate : mise à jour partielle, un champ absent ou null est ignoré. */
public record UserUpdateRequest(
        @Size(min = 1, max = 120, message = "entre 1 et 120 caractères") String fullName,
        @Size(max = 500, message = "500 caractères maximum") String avatarUrl,
        @Pattern(regexp = "fr|ar|en", message = "doit valoir fr, ar ou en") String preferredLanguage) {
}
