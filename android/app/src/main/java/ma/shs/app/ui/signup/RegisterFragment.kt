package ma.shs.app.ui.signup

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import ma.shs.app.R
import ma.shs.app.databinding.FragmentRegisterBinding
import ma.shs.app.ui.common.collectWhenStarted

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val viewModel: SignupViewModel by navGraphViewModels(R.id.nav_signup)
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRegisterBinding.bind(view)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        // TODO(S1-09) : naviguer vers Connexion quand cet écran existera (lot auth, issue S1-09).
        binding.btnLogin.setOnClickListener { }
        binding.btnSubmit.setOnClickListener { submit() }

        binding.etName.doAfterTextChanged { binding.tilName.error = null }
        binding.etEmail.doAfterTextChanged { binding.tilEmail.error = null }
        binding.etPassword.doAfterTextChanged { binding.tilPassword.error = null }
        binding.cbTerms.setOnCheckedChangeListener { _, _ -> binding.tvTermsError.isVisible = false }

        collectWhenStarted(viewModel.errors) { render(it) }
    }

    private fun submit() {
        val form = RegisterForm(
            fullName = binding.etName.text?.toString().orEmpty(),
            email = binding.etEmail.text?.toString().orEmpty(),
            password = binding.etPassword.text?.toString().orEmpty(),
            phone = binding.etPhone.text?.toString().orEmpty().trim(),
            termsAccepted = binding.cbTerms.isChecked
        )
        if (viewModel.submit(form)) {
            findNavController().navigate(R.id.action_register_to_role)
        }
    }

    private fun render(errors: RegisterErrors) {
        binding.tilName.error = if (errors.fullName) getString(R.string.auth_signup_error_name) else null
        binding.tilEmail.error = if (errors.email) getString(R.string.auth_signup_error_email) else null
        binding.tilPassword.error = if (errors.password) getString(R.string.auth_signup_error_password) else null
        binding.tvTermsError.isVisible = errors.terms
        binding.tvSubtitle.setText(
            if (errors.hasAny) R.string.auth_signup_subtitle_errors else R.string.auth_signup_subtitle
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
