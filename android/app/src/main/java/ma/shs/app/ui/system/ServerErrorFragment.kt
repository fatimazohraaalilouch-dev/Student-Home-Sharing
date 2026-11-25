package ma.shs.app.ui.system

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import ma.shs.app.R
import ma.shs.app.databinding.FragmentStateServerErrorBinding

class ServerErrorFragment : Fragment(R.layout.fragment_state_server_error) {

    private var _binding: FragmentStateServerErrorBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentStateServerErrorBinding.bind(view)
        binding.btnRetry.setOnClickListener {
            StateResult.send(this, StateResult.RETRY)
            NavHostFragment.findNavController(this).popBackStack()
        }
        binding.btnHome.setOnClickListener {
            // TODO(S2-B3) : naviguer vers l'accueil Explorer quand il existera
            StateResult.send(this, StateResult.HOME)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}