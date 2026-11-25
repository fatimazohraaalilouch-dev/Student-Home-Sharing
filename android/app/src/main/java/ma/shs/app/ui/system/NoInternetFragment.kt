package ma.shs.app.ui.system

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import ma.shs.app.R
import ma.shs.app.databinding.FragmentStateNoInternetBinding

/** Clés du résultat renvoyé à l'écran appelant quand l'utilisateur choisit « Réessayer ». */
object StateResult {
    const val KEY = "state_result"
    const val ACTION = "action"
    const val RETRY = "retry"
}

class NoInternetFragment : Fragment(R.layout.fragment_state_no_internet) {

    private var _binding: FragmentStateNoInternetBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentStateNoInternetBinding.bind(view)
        binding.btnRetry.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                StateResult.KEY,
                Bundle().apply { putString(StateResult.ACTION, StateResult.RETRY) }
            )
            NavHostFragment.findNavController(this).popBackStack()
        }
        binding.btnBack.setOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}