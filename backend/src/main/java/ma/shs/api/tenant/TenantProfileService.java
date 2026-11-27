package ma.shs.api.tenant;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import ma.shs.api.common.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Profil de recherche du locataire : une ligne par utilisateur (clé primaire user_id). */
@Service
public class TenantProfileService {

    private static final Set<String> LISTING_TYPES = Set.of("ROOM", "STUDIO", "APARTMENT", "SHARED_ROOM");

    private final NamedParameterJdbcTemplate jdbc;

    public TenantProfileService(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public TenantProfileDto get(UUID userId) {
        List<TenantProfileDto> rows = jdbc.query(
                "SELECT establishment_id, budget_min, budget_max, max_distance_km, "
                        + "preferred_types, required_amenities, importance_budget, "
                        + "importance_distance, importance_amenities, importance_rating "
                        + "FROM tenant_profile WHERE user_id = :id",
                Map.of("id", userId),
                (rs, i) -> new TenantProfileDto(
                        rs.getObject("establishment_id", UUID.class),
                        rs.getBigDecimal("budget_min"),
                        rs.getBigDecimal("budget_max"),
                        rs.getBigDecimal("max_distance_km"),
                        toList(rs.getArray("preferred_types")),
                        toList(rs.getArray("required_amenities")),
                        rs.getInt("importance_budget"),
                        rs.getInt("importance_distance"),
                        rs.getInt("importance_amenities"),
                        rs.getInt("importance_rating")));
        if (rows.isEmpty()) {
            throw ApiException.notFound("Profil de recherche introuvable");
        }
        return rows.get(0);
    }

    @Transactional
    public TenantProfileDto save(UUID userId, TenantProfileDto in) {
        validateBusinessRules(in);
        List<String> types = in.preferredTypes() == null ? List.of() : in.preferredTypes();
        List<String> amenities = in.requiredAmenities() == null ? List.of() : in.requiredAmenities();
        // PUT remplace la ressource : un champ d'importance absent reprend la valeur par défaut.
        jdbc.update(
                "INSERT INTO tenant_profile (user_id, establishment_id, budget_min, budget_max, "
                        + "max_distance_km, preferred_types, required_amenities, importance_budget, "
                        + "importance_distance, importance_amenities, importance_rating) "
                        + "VALUES (:userId, :est, :bmin, :bmax, :dist, "
                        + "COALESCE(string_to_array(NULLIF(CAST(:types AS text), ''), ','), '{}'), "
                        + "COALESCE(string_to_array(NULLIF(CAST(:amen AS text), ''), ','), '{}'), "
                        + ":ib, :idist, :ia, :ir) "
                        + "ON CONFLICT (user_id) DO UPDATE SET "
                        + "establishment_id = EXCLUDED.establishment_id, budget_min = EXCLUDED.budget_min, "
                        + "budget_max = EXCLUDED.budget_max, max_distance_km = EXCLUDED.max_distance_km, "
                        + "preferred_types = EXCLUDED.preferred_types, "
                        + "required_amenities = EXCLUDED.required_amenities, "
                        + "importance_budget = EXCLUDED.importance_budget, "
                        + "importance_distance = EXCLUDED.importance_distance, "
                        + "importance_amenities = EXCLUDED.importance_amenities, "
                        + "importance_rating = EXCLUDED.importance_rating, updated_at = now()",
                new MapSqlParameterSource()
                        .addValue("userId", userId)
                        .addValue("est", in.establishmentId())
                        .addValue("bmin", in.budgetMin())
                        .addValue("bmax", in.budgetMax())
                        .addValue("dist", in.maxDistanceKm())
                        .addValue("types", String.join(",", types))
                        .addValue("amen", String.join(",", amenities))
                        .addValue("ib", orDefault(in.importanceBudget(), 90))
                        .addValue("idist", orDefault(in.importanceDistance(), 75))
                        .addValue("ia", orDefault(in.importanceAmenities(), 70))
                        .addValue("ir", orDefault(in.importanceRating(), 40)));
        return get(userId);
    }

    /** Règles qui ne s'expriment pas avec les annotations : références, listes, budgets. */
    private void validateBusinessRules(TenantProfileDto in) {
        Map<String, Object> errors = new LinkedHashMap<>();

        Integer found = jdbc.queryForObject(
                "SELECT count(*) FROM establishment WHERE id = :id",
                Map.of("id", in.establishmentId()), Integer.class);
        if (found == null || found == 0) {
            errors.put("establishmentId", "établissement inconnu");
        }
        if (in.budgetMin() != null && in.budgetMax() != null
                && in.budgetMin().compareTo(in.budgetMax()) > 0) {
            errors.put("budgetMin", "doit être inférieur ou égal à budgetMax");
        }
        if (in.preferredTypes() != null) {
            for (String t : in.preferredTypes()) {
                if (t == null || !LISTING_TYPES.contains(t)) {
                    errors.put("preferredTypes", "valeur inconnue : " + t);
                    break;
                }
            }
        }
        if (in.requiredAmenities() != null) {
            Set<String> known = Set.copyOf(jdbc.queryForList(
                    "SELECT code FROM amenity", Map.of(), String.class));
            for (String a : in.requiredAmenities()) {
                if (a == null || !known.contains(a)) {
                    errors.put("requiredAmenities", "valeur inconnue : " + a);
                    break;
                }
            }
        }
        if (!errors.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Données invalides", errors);
        }
    }

    private static int orDefault(Integer value, int fallback) {
        return value != null ? value : fallback;
    }

    private static List<String> toList(java.sql.Array array) throws java.sql.SQLException {
        if (array == null) {
            return List.of();
        }
        return List.of((String[]) array.getArray());
    }
}
