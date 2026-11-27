package ma.shs.api.user;

/** Schéma NotificationPreference : une clé de {@link NotificationKeys} et son état. */
public record NotificationPreferenceDto(String key, Boolean enabled) {
}
