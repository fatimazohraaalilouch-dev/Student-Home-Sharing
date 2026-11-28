package ma.shs.app.ui.signup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SignupTest {

    private fun form(
        name: String = "Salma El Amrani",
        email: String = "salma@exemple.ma",
        password: String = "motdepasse8",
        phone: String = "",
        terms: Boolean = true
    ) = RegisterForm(name, email, password, phone, terms)

    @Test
    fun formulaireValide_aucuneErreur() {
        assertFalse(SignupValidator.validate(form()).hasAny)
    }

    @Test
    fun nomVideOuEspaces_erreurNom() {
        assertTrue(SignupValidator.validate(form(name = "   ")).fullName)
    }

    @Test
    fun emailInvalide_erreurEmail() {
        assertTrue(SignupValidator.validate(form(email = "salma@")).email)
        assertTrue(SignupValidator.validate(form(email = "salma.exemple.ma")).email)
        assertTrue(SignupValidator.validate(form(email = "")).email)
    }

    @Test
    fun emailAvecEspacesAutour_estAccepte() {
        assertFalse(SignupValidator.validate(form(email = "  salma@exemple.ma ")).email)
    }

    @Test
    fun motDePasseDe7Caracteres_erreur_et8Caracteres_ok() {
        assertTrue(SignupValidator.validate(form(password = "1234567")).password)
        assertFalse(SignupValidator.validate(form(password = "12345678")).password)
    }

    @Test
    fun conditionsNonAcceptees_erreurConditions() {
        assertTrue(SignupValidator.validate(form(terms = false)).terms)
    }

    @Test
    fun telephoneFacultatif_nEstJamaisEnErreur() {
        assertFalse(SignupValidator.validate(form(phone = "")).hasAny)
        assertFalse(SignupValidator.validate(form(phone = "+212 6 12 34 56 78")).hasAny)
    }

    @Test
    fun viewModel_formulaireInvalide_gardeLesErreursEtPasDeFormulaire() {
        val vm = SignupViewModel()
        assertFalse(vm.submit(form(email = "x")))
        assertTrue(vm.errors.value.email)
        assertNull(vm.validForm)
    }

    @Test
    fun viewModel_formulaireValide_effaceLesErreursEtGardeLeFormulaire() {
        val vm = SignupViewModel()
        vm.submit(form(email = "x"))
        assertTrue(vm.submit(form()))
        assertFalse(vm.errors.value.hasAny)
        assertNotNull(vm.validForm)
    }

    @Test
    fun viewModel_valeursParDefaut_etudiantEtFrancais() {
        val vm = SignupViewModel()
        assertEquals(AccountSpace.TENANT, vm.space.value)
        assertEquals(AppLanguage.FR, vm.language.value)
    }

    @Test
    fun viewModel_choixDuRoleEtDeLaLangue() {
        val vm = SignupViewModel()
        vm.selectSpace(AccountSpace.OWNER)
        vm.selectLanguage(AppLanguage.AR)
        assertEquals(AccountSpace.OWNER, vm.space.value)
        assertEquals("ar", vm.language.value.code)
    }
}
