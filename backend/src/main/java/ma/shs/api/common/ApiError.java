package ma.shs.api.common;

import java.util.Map;

/** Corps JSON de toutes les erreurs : schéma Error de docs/openapi.yaml. */
public record ApiError(String code, String message, Map<String, Object> details) {
}