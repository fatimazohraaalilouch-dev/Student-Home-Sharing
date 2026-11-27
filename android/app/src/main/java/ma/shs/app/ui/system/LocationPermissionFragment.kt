package ma.shs.app.ui.system

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import ma.shs.app.R
import ma.shs.app.databinding.FragmentPermLocationBinding

class LocationPermissionFragment : Fragment(R.layout.fragment_perm_location) {

    private var _binding: FragmentPermLocationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPermLocationBinding.bind(view)
        binding.btnAllow.setOnClickListener { answer(PermissionResult.ALLOW) }
        binding.btnOnce.setOnClickListener { answer(PermissionResult.ALLOW_ONCE) }
        binding.btnDeny.setOnClickListener { answer(PermissionResult.DENY) }
    }

    private fun answer(choice: String) {
        PermissionResult.send(this, PermissionResult.LOCATION, choice)
        NavHostFragment.findNavController(this).popBackStack()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}