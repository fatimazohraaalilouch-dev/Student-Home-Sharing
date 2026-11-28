package ma.shs.app.ui.signup

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ma.shs.app.common.BaseViewModel

/**
 * État commun des écrans Inscription, Rôle et Langue (partagé par le graphe nav_signup).
 * Aucun appel réseau dans S1-10 : l'envoi (POST /auth/register, PUT /me, etc.) est branché en S1-13.
 */
class SignupViewModel : BaseViewModel() {

    private val _errors = MutableStateFlow(RegisterErrors())
    val errors: StateFlow<RegisterErrors> = _errors

    private val _space = MutableStateFlow(AccountSpace.TENANT)
    val space: StateFlow<AccountSpace> = _space

    private val _language = MutableStateFlow(AppLanguage.FR)
    val language: StateFlow<AppLanguage> = _language

    /** Formulaire validé, gardé pour l'envoi en S1-13. */
    var validForm: RegisterForm? = null
        private set

    /** Vérifie le formulaire ; renvoie true s'il est valide. */
    fun submit(form: RegisterForm): Boolean {
        val result = SignupValidator.validate(form)
        _errors.value = result
        validForm = if (result.hasAny) null else form
        return !result.hasAny
    }

    fun selectSpace(space: AccountSpace) {
        _space.value = space
    }

    fun selectLanguage(language: AppLanguage) {
        _language.value = language
    }
}
