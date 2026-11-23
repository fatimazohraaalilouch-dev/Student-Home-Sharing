package ma.shs.app.common

import android.content.Context
import androidx.annotation.StringRes
import ma.shs.app.R

/** Texte à afficher pour une erreur : choisi à partir du code, jamais du message brut du serveur. */
@StringRes
fun AppError.messageRes(): Int = when (code) {
    ApiErrorCode.NETWORK -> R.string.error_network
    ApiErrorCode.SESSION_EXPIRED -> R.string.error_session_expired
    ApiErrorCode.INVALID_CREDENTIALS -> R.string.error_invalid_credentials
    ApiErrorCode.VALIDATION_ERROR -> R.string.error_validation
    ApiErrorCode.FORBIDDEN -> R.string.error_forbidden
    ApiErrorCode.NOT_FOUND -> R.string.error_not_found
    ApiErrorCode.BOOKING_DATES_CONFLICT -> R.string.error_booking_dates_conflict
    ApiErrorCode.CONSENT_REQUIRED -> R.string.error_consent_required
    ApiErrorCode.PROOF_REQUIRED -> R.string.error_proof_required
    ApiErrorCode.COPILOT_RATE_LIMIT -> R.string.error_copilot_rate_limit_generic
    ApiErrorCode.SERVER_ERROR -> R.string.error_server
    ApiErrorCode.UNKNOWN -> R.string.error_unknown
}

fun AppError.toText(context: Context): String {
    val retry = retryAfterSeconds
    return if (code == ApiErrorCode.COPILOT_RATE_LIMIT && retry != null)
        context.getString(R.string.error_copilot_rate_limit, retry)
    else
        context.getString(messageRes())
}