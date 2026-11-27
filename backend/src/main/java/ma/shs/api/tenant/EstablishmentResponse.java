package ma.shs.api.tenant;

import java.util.UUID;

/** Schéma Establishment de docs/openapi.yaml. */
public record EstablishmentResponse(
        UUID id,
        String name,
        String universityName,
        String addressText,
        String city,
        double latitude,
        double longitude) {
}
