package ma.shs.app.ui.system

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ma.shs.app.R
import ma.shs.app.databinding.FragmentStateSessionExpiredBinding

class SessionExpiredFragment : Fragment(R.layout.fragment_state_session_expired) {

    private var _binding: FragmentStateSessionExpiredBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentStateSessionExpiredBinding.bind(view)
        binding.btnLogin.setOnClickListener {
            // TODO(S1-12) : naviguer vers Connexion quand l'écran sera branché
            StateResult.send(this, StateResult.LOGIN)
        }
        binding.btnExplore.setOnClickListener {
            // TODO(S2-B3) : naviguer vers l'accueil Explorer quand il existera
            StateResult.send(this, StateResult.EXPLORE)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}