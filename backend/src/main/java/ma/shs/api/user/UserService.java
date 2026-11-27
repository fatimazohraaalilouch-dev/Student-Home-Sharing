package ma.shs.api.user;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import ma.shs.api.common.ApiException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Profil de l'utilisateur connecté, espaces et réglages de notifications. */
@Service
public class UserService {

    private final NamedParameterJdbcTemplate jdbc;

    public UserService(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public UserResponse get(UUID userId) {
        List<UserResponse> rows = jdbc.query(
                "SELECT id, email, full_name, phone, avatar_url, preferred_language, active_space "
                        + "FROM app_user WHERE id = :id AND deleted_at IS NULL",
                Map.of("id", userId),
                (rs, i) -> new UserResponse(
                        rs.getObject("id", UUID.class),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("avatar_url"),
                        rs.getString("preferred_language"),
                        rs.getString("active_space"),
                        List.of()));
        if (rows.isEmpty()) {
            throw ApiException.notFound("Utilisateur introuvable");
        }
        UserResponse u = rows.get(0);
        List<String> spaces = jdbc.queryForList(
                "SELECT space FROM user_space WHERE user_id = :id ORDER BY space DESC",
                Map.of("id", userId), String.class);
        return new UserResponse(u.id(), u.email(), u.fullName(), u.phone(), u.avatarUrl(),
                u.preferredLanguage(), u.activeSpace(), spaces);
    }

    @Transactional
    public UserResponse update(UUID userId, UserUpdateRequest req) {
        if (req.fullName() != null && req.fullName().isBlank()) {
            throw ApiException.validation("fullName", "ne peut pas être vide");
        }
        int n = jdbc.update(
                "UPDATE app_user SET full_name = COALESCE(:fullName, full_name), "
                        + "avatar_url = COALESCE(:avatarUrl, avatar_url), "
                        + "preferred_language = COALESCE(:lang, preferred_language), "
                        + "updated_at = now() WHERE id = :id AND deleted_at IS NULL",
                new MapSqlParameterSource()
                        .addValue("fullName", req.fullName() == null ? null : req.fullName().trim())
                        .addValue("avatarUrl", req.avatarUrl())
                        .addValue("lang", req.preferredLanguage())
                        .addValue("id", userId));
        if (n == 0) {
            throw ApiException.notFound("Utilisateur introuvable");
        }
        return get(userId);
    }

    /** Suppression logique : la purge définitive est prévue plus tard (S4-05). */
    @Transactional
    public void delete(UUID userId) {
        int n = jdbc.update(
                "UPDATE app_user SET deleted_at = now(), is_active = false, updated_at = now() "
                        + "WHERE id = :id AND deleted_at IS NULL",
                Map.of("id", userId));
        if (n == 0) {
            throw ApiException.notFound("Utilisateur introuvable");
        }
        jdbc.update("UPDATE refresh_token SET revoked_at = now() "
                + "WHERE user_id = :id AND revoked_at IS NULL", Map.of("id", userId));
    }

    @Transactional
    public UserResponse changeActiveSpace(UUID userId, Space space) {
        get(userId);
        Integer enabled = jdbc.queryForObject(
                "SELECT count(*) FROM user_space WHERE user_id = :id AND space = :space",
                Map.of("id", userId, "space", space.name()), Integer.class);
        if (enabled == null || enabled == 0) {
            throw ApiException.forbidden("Espace non activé pour ce compte");
        }
        jdbc.update("UPDATE app_user SET active_space = :space, updated_at = now() WHERE id = :id",
                Map.of("id", userId, "space", space.name()));
        return get(userId);
    }

    /** Active l'espace propriétaire. Sans effet s'il l'est déjà. */
    @Transactional
    public void enableOwnerSpace(UUID userId) {
        get(userId);
        jdbc.update("INSERT INTO user_space (user_id, space) VALUES (:id, 'OWNER') "
                + "ON CONFLICT (user_id, space) DO NOTHING", Map.of("id", userId));
    }

    public List<NotificationPreferenceDto> notificationPreferences(UUID userId) {
        Map<String, Boolean> stored = new LinkedHashMap<>();
        jdbc.query("SELECT key, enabled FROM notification_preference WHERE user_id = :id",
                Map.of("id", userId),
                (RowCallbackHandler) rs -> {
                    stored.put(rs.getString("key"), rs.getBoolean("enabled"));
                });
        List<NotificationPreferenceDto> result = new ArrayList<>();
        NotificationKeys.DEFAULTS.forEach((key, def) ->
                result.add(new NotificationPreferenceDto(key, stored.getOrDefault(key, def))));
        return result;
    }

    @Transactional
    public List<NotificationPreferenceDto> updateNotificationPreferences(
            UUID userId, List<NotificationPreferenceDto> prefs) {
        get(userId);
        List<NotificationPreferenceDto> input = prefs == null ? List.of() : prefs;
        Map<String, Object> errors = new LinkedHashMap<>();
        for (int i = 0; i < input.size(); i++) {
            NotificationPreferenceDto p = input.get(i);
            if (p == null || p.key() == null || !NotificationKeys.DEFAULTS.containsKey(p.key())) {
                errors.put("[" + i + "].key", "clé inconnue");
            }
            if (p == null || p.enabled() == null) {
                errors.put("[" + i + "].enabled", "obligatoire");
            }
        }
        if (!errors.isEmpty()) {
            throw new ApiException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "VALIDATION_ERROR", "Données invalides", errors);
        }
        for (NotificationPreferenceDto p : input) {
            jdbc.update("INSERT INTO notification_preference (user_id, key, enabled) "
                    + "VALUES (:id, :key, :enabled) "
                    + "ON CONFLICT (user_id, key) DO UPDATE SET enabled = EXCLUDED.enabled",
                    Map.of("id", userId, "key", p.key(), "enabled", p.enabled()));
        }
        return notificationPreferences(userId);
    }
}
