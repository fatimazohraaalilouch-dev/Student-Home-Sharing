package ma.shs.app.ui.system

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import ma.shs.app.R
import ma.shs.app.databinding.FragmentAccountLogoutBinding

class LogoutFragment : Fragment(R.layout.fragment_account_logout) {

    private var _binding: FragmentAccountLogoutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAccountLogoutBinding.bind(view)
        binding.btnLogout.setOnClickListener {
            // TODO : effacer les jetons (TokenStore) puis ouvrir Connexion quand l'écran sera branché
            AccountResult.send(this, AccountResult.LOGOUT)
        }
        binding.btnStay.setOnClickListener {
            AccountResult.send(this, AccountResult.STAY)
            NavHostFragment.findNavController(this).popBackStack()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}