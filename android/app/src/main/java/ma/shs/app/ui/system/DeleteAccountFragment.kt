package ma.shs.app.ui.system

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import ma.shs.app.R
import ma.shs.app.databinding.FragmentAccountDeleteBinding

class DeleteAccountFragment : Fragment(R.layout.fragment_account_delete) {

    private var _binding: FragmentAccountDeleteBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAccountDeleteBinding.bind(view)
        binding.btnDelete.setOnClickListener {
            if (binding.etPassword.text.isNullOrEmpty()) {
                binding.passwordLayout.error = getString(R.string.account_delete_password_required)
            } else {
                binding.passwordLayout.error = null
                // TODO : appeler la route de suppression de compte (docs/openapi.yaml) avec le mot de passe
                AccountResult.send(this, AccountResult.DELETE)
            }
        }
        binding.btnKeep.setOnClickListener {
            AccountResult.send(this, AccountResult.KEEP)
            NavHostFragment.findNavController(this).popBackStack()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}