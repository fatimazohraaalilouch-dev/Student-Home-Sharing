package ma.shs.api.tenant;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Schéma TenantProfile de docs/openapi.yaml (requête et réponse).
 * Les bornes reprennent les contraintes de la table tenant_profile.
 * budgetMin est facultatif (décision 4) ; un champ d'importance absent prend la valeur
 * par défaut de la base (90, 75, 70, 40).
 */
public record TenantProfileDto(
        @NotNull(message = "obligatoire") UUID establishmentId,
        @DecimalMin(value = "0", message = "doit être positif ou nul")
        @DecimalMax(value = "99999999.99", message = "trop grand") BigDecimal budgetMin,
        @NotNull(message = "obligatoire")
        @DecimalMin(value = "0", inclusive = false, message = "doit être supérieur à 0")
        @DecimalMax(value = "99999999.99", message = "trop grand") BigDecimal budgetMax,
        @NotNull(message = "obligatoire")
        @DecimalMin(value = "0", inclusive = false, message = "doit être supérieur à 0")
        @DecimalMax(value = "999.99", message = "999,99 km au plus") BigDecimal maxDistanceKm,
        List<String> preferredTypes,
        List<String> requiredAmenities,
        @Min(value = 0, message = "entre 0 et 100") @Max(value = 100, message = "entre 0 et 100")
        Integer importanceBudget,
        @Min(value = 0, message = "entre 0 et 100") @Max(value = 100, message = "entre 0 et 100")
        Integer importanceDistance,
        @Min(value = 0, message = "entre 0 et 100") @Max(value = 100, message = "entre 0 et 100")
        Integer importanceAmenities,
        @Min(value = 0, message = "entre 0 et 100") @Max(value = 100, message = "entre 0 et 100")
        Integer importanceRating) {
}
