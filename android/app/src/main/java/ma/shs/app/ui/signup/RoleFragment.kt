package ma.shs.app.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import ma.shs.app.R
import ma.shs.app.databinding.FragmentRoleBinding
import ma.shs.app.ui.common.applySelected
import ma.shs.app.ui.common.collectWhenStarted

class RoleFragment : Fragment(R.layout.fragment_role) {

    private val viewModel: SignupViewModel by navGraphViewModels(R.id.nav_signup)
    private var _binding: FragmentRoleBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRoleBinding.bind(view)

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.cardStudent.setOnClickListener { viewModel.selectSpace(AccountSpace.TENANT) }
        binding.cardOwner.setOnClickListener { viewModel.selectSpace(AccountSpace.OWNER) }
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_role_to_language)
        }

        collectWhenStarted(viewModel.space) { render(it) }
    }

    private fun render(space: AccountSpace) {
        binding.cardStudent.applySelected(binding.ivStudentCheck, space == AccountSpace.TENANT)
        binding.cardOwner.applySelected(binding.ivOwnerCheck, space == AccountSpace.OWNER)
        binding.btnContinue.setText(
            if (space == AccountSpace.TENANT) R.string.auth_role_continue_student
            else R.string.auth_role_continue_owner
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
