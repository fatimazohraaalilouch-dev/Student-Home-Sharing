package ma.shs.app.common

/** Codes d'erreur de docs/openapi.yaml (schéma Error) + deux codes locaux (NETWORK, UNKNOWN). */
enum class ApiErrorCode {
    VALIDATION_ERROR,
    INVALID_CREDENTIALS,
    SESSION_EXPIRED,
    FORBIDDEN,
    NOT_FOUND,
    BOOKING_DATES_CONFLICT,
    CONSENT_REQUIRED,
    PROOF_REQUIRED,
    COPILOT_RATE_LIMIT,
    SERVER_ERROR,

    /** Local : pas de réseau, délai dépassé, serveur injoignable. */
    NETWORK,

    /** Local : code absent ou inconnu. */
    UNKNOWN;

    companion object {
        fun fromRaw(raw: String?): ApiErrorCode? = values().firstOrNull { it.name == raw }
    }
}