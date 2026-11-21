package ma.shs.app.common

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ErrorMapperTest {

    private fun http(status: Int, json: String): HttpException =
        HttpException(Response.error<Any>(status, json.toResponseBody("application/json".toMediaType())))

    @Test
    fun conflitDeDates_409() {
        val e = ErrorMapper.map(http(409, """{"code":"BOOKING_DATES_CONFLICT","message":"x"}"""))
        assertEquals(ApiErrorCode.BOOKING_DATES_CONFLICT, e.code)
        assertEquals(409, e.httpStatus)
    }

    @Test
    fun codeDuCorps_priorite_surLeStatut() {
        val e = ErrorMapper.map(http(401, """{"code":"INVALID_CREDENTIALS","message":"x"}"""))
        assertEquals(ApiErrorCode.INVALID_CREDENTIALS, e.code)
    }

    @Test
    fun corpsVide_401_donneSessionExpiree() {
        assertEquals(ApiErrorCode.SESSION_EXPIRED, ErrorMapper.map(http(401, "")).code)
    }

    @Test
    fun limiteCopilot_lisRetryAfterSeconds() {
        val e = ErrorMapper.map(http(429, """{"code":"COPILOT_RATE_LIMIT","message":"x","details":{"retryAfterSeconds":30}}"""))
        assertEquals(ApiErrorCode.COPILOT_RATE_LIMIT, e.code)
        assertEquals(30L, e.retryAfterSeconds)
    }

    @Test
    fun validation_exposeLesErreursParChamp() {
        val e = ErrorMapper.map(http(400, """{"code":"VALIDATION_ERROR","message":"x","details":{"email":"invalide"}}"""))
        assertEquals("invalide", e.fieldErrors["email"])
    }

    @Test
    fun erreur500_sansCode_donneServerError() {
        assertEquals(ApiErrorCode.SERVER_ERROR, ErrorMapper.map(http(500, "<html>oops</html>")).code)
    }

    @Test
    fun pasDeReseau_donneNetwork() {
        val e = ErrorMapper.map(IOException("timeout"))
        assertEquals(ApiErrorCode.NETWORK, e.code)
        assertNull(e.httpStatus)
    }
}