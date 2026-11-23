package ma.shs.app.common

import com.google.gson.Gson
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.io.IOException

/** Corps JSON des erreurs du backend : {code, message, details}. */
internal data class ApiErrorBody(
    val code: String?,
    val message: String?,
    val details: Map<String, Any?>?
)

object ErrorMapper {
    private val gson = Gson()

    fun map(t: Throwable): AppError = when (t) {
        is HttpException -> fromHttp(t)
        is IOException -> AppError(ApiErrorCode.NETWORK)
        else -> AppError(ApiErrorCode.UNKNOWN, t.message)
    }

    private fun fromHttp(e: HttpException): AppError {
        val status = e.code()
        val body = parseBody(e)
        val code = ApiErrorCode.fromRaw(body?.code) ?: fallback(status)
        return AppError(code, body?.message, body?.details.orEmpty(), status)
    }

    private fun parseBody(e: HttpException): ApiErrorBody? {
        return try {
            val raw = e.response()?.errorBody()?.string()
            if (raw.isNullOrBlank()) null else gson.fromJson(raw, ApiErrorBody::class.java)
        } catch (ex: JsonParseException) {
            null
        } catch (ex: IOException) {
            null
        }
    }

    /** Si le corps ne contient pas de code connu, on déduit du statut HTTP. */
    private fun fallback(status: Int): ApiErrorCode = when {
        status == 400 -> ApiErrorCode.VALIDATION_ERROR
        status == 401 -> ApiErrorCode.SESSION_EXPIRED
        status == 403 -> ApiErrorCode.FORBIDDEN
        status == 404 -> ApiErrorCode.NOT_FOUND
        status == 429 -> ApiErrorCode.COPILOT_RATE_LIMIT
        status >= 500 -> ApiErrorCode.SERVER_ERROR
        else -> ApiErrorCode.UNKNOWN
    }
}