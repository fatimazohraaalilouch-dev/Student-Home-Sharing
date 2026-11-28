package ma.shs.app.ui.signup

/** Les deux espaces d'un compte (Space dans docs/openapi.yaml). */
enum class AccountSpace { TENANT, OWNER }

/** Langues proposées (preferredLanguage dans l'API). */
enum class AppLanguage(val code: String) { FR("fr"), AR("ar"), EN("en") }

data class RegisterForm(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String,
    val termsAccepted: Boolean
)

/** true = le champ est en erreur. */
data class RegisterErrors(
    val fullName: Boolean = false,
    val email: Boolean = false,
    val password: Boolean = false,
    val terms: Boolean = false
) {
    val hasAny: Boolean get() = fullName || email || password || terms
}

/** Règles de l'écran Inscription, alignées sur RegisterRequest (e-mail valide, mot de passe de 8 caractères au moins). */
object SignupValidator {
    const val PASSWORD_MIN_LENGTH = 8
    private val EMAIL = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

    fun validate(form: RegisterForm): RegisterErrors = RegisterErrors(
        fullName = form.fullName.isBlank(),
        email = !EMAIL.matches(form.email.trim()),
        password = form.password.length < PASSWORD_MIN_LENGTH,
        terms = !form.termsAccepted
    )
}
