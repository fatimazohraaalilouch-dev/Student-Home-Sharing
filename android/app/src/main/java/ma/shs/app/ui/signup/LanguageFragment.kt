package ma.shs.app.ui.signup

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import ma.shs.app.R
import ma.shs.app.databinding.FragmentLanguageBinding
import ma.shs.app.ui.common.applySelected
import ma.shs.app.ui.common.collectWhenStarted

class LanguageFragment : Fragment(R.layout.fragment_language) {

    private val viewModel: SignupViewModel by navGraphViewModels(R.id.nav_signup)
    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentLanguageBinding.bind(view)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.cardFr.setOnClickListener { viewModel.selectLanguage(AppLanguage.FR) }
        binding.cardAr.setOnClickListener { viewModel.selectLanguage(AppLanguage.AR) }
        binding.cardEn.setOnClickListener { viewModel.selectLanguage(AppLanguage.EN) }
        // TODO(S1-13) : envoyer la langue avec PATCH /me.
        binding.btnContinue.setOnClickListener { continueFlow() }

        collectWhenStarted(viewModel.language) { render(it) }
    }

    /** Étudiant : Profil locataire 1 (décision 10). Propriétaire : espace propriétaire, sans profil locataire. */
    private fun continueFlow() {
        val action = if (viewModel.space.value == AccountSpace.OWNER) {
            R.id.action_language_to_owner
        } else {
            R.id.action_language_to_tenant_profile
        }
        findNavController().navigate(action)
        // Après la navigation : le changement de langue recrée l'activité, qui restaure alors l'écran suivant.
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(viewModel.language.value.code))
    }

    private fun render(language: AppLanguage) {
        binding.cardFr.applySelected(binding.ivFrCheck, language == AppLanguage.FR)
        binding.cardAr.applySelected(binding.ivArCheck, language == AppLanguage.AR)
        binding.cardEn.applySelected(binding.ivEnCheck, language == AppLanguage.EN)
        val label = when (language) {
            AppLanguage.FR -> R.string.auth_language_label_fr
            AppLanguage.AR -> R.string.auth_language_label_ar
            AppLanguage.EN -> R.string.auth_language_label_en
        }
        binding.btnContinue.text = getString(R.string.auth_language_continue, getString(label))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
