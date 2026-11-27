package ma.shs.app.ui.system

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ma.shs.app.R
import ma.shs.app.databinding.FragmentAccountDeletedBinding

class AccountDeletedFragment : Fragment(R.layout.fragment_account_deleted) {

    private var _binding: FragmentAccountDeletedBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentAccountDeletedBinding.bind(view)
        binding.btnHome.setOnClickListener {
            // TODO(S2-B3) : naviguer vers l'accueil quand il existera
            AccountResult.send(this, AccountResult.HOME)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}