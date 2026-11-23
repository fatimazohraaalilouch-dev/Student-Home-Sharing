package ma.shs.app.common

/**
 * Erreur unique vue par les ViewModels et les écrans.
 * L'écran choisit son texte à partir de [code] (FR/AR/EN), jamais à partir de [message].
 */
data class AppError(
    val code: ApiErrorCode,
    val message: String? = null,
    val details: Map<String, Any?> = emptyMap(),
    val httpStatus: Int? = null
) {
    /** Renseigné pour COPILOT_RATE_LIMIT (details.retryAfterSeconds). */
    val retryAfterSeconds: Long?
        get() = (details["retryAfterSeconds"] as? Number)?.toLong()

    /** Erreurs par champ pour VALIDATION_ERROR (details = {champ: message}). */
    val fieldErrors: Map<String, String>
        get() = details.mapNotNull { (k, v) -> (v as? String)?.let { k to it } }.toMap()
}