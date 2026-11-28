package ma.shs.app.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import ma.shs.app.R
import ma.shs.app.databinding.FragmentProfileEstablishmentBinding
import ma.shs.app.ui.common.collectWhenStarted

/** Profil locataire 1/3 : choix de l'établissement. */
class ProfileEstablishmentFragment : Fragment(R.layout.fragment_profile_establishment) {

    private val viewModel: TenantProfileViewModel by navGraphViewModels(R.id.nav_tenant_profile)
    private var _binding: FragmentProfileEstablishmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentProfileEstablishmentBinding.bind(view)
        viewModel.setCatalog(SampleEstablishments.all(requireContext()))

        binding.tvStep.text = getString(R.string.search_profile_step, 1, 3)
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.etSearch.doAfterTextChanged { viewModel.search(it?.toString().orEmpty()) }
        binding.btnContinue.setOnClickListener {
            if (viewModel.draft.value.establishment == null) {
                binding.tilSearch.error = getString(R.string.search_profile_1_error)
            } else {
                findNavController().navigate(R.id.action_profile_1_to_2)
            }
        }

        collectWhenStarted(viewModel.results) { renderResults(it, viewModel.draft.value.establishment) }
        collectWhenStarted(viewModel.draft) { renderResults(viewModel.results.value, it.establishment) }
    }

    private fun renderResults(results: List<EstablishmentUi>, selected: EstablishmentUi?) {
        val container = binding.llResults
        container.removeAllViews()
        binding.tvNoResult.isVisible = results.isEmpty()
        results.forEach { item ->
            val row = layoutInflater.inflate(R.layout.item_establishment, container, false)
            val isSelected = item.id == selected?.id
            val name = row.findViewById<TextView>(R.id.tv_name)
            val desc = row.findViewById<TextView>(R.id.tv_desc)
            name.text = item.name
            name.setTextColor(
                ContextCompat.getColor(
                    requireContext(), if (isSelected) R.color.shs_teal_accent else R.color.shs_on_background
                )
            )
            desc.text = if (isSelected) {
                getString(R.string.search_profile_1_selected_tag).let { tag -> "${item.description} · $tag" }
            } else {
                item.description
            }
            row.setOnClickListener {
                binding.tilSearch.error = null
                viewModel.selectEstablishment(item)
            }
            container.addView(row)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
