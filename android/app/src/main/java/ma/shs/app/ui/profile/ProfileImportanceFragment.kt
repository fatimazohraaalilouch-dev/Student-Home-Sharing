package ma.shs.app.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.slider.Slider
import ma.shs.app.R
import ma.shs.app.databinding.FragmentProfileImportanceBinding

/** Profil locataire 3/3 : importance des 4 critères (0 à 100). */
class ProfileImportanceFragment : Fragment(R.layout.fragment_profile_importance) {

    private val viewModel: TenantProfileViewModel by navGraphViewModels(R.id.nav_tenant_profile)
    private var _binding: FragmentProfileImportanceBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentProfileImportanceBinding.bind(view)
        val draft = viewModel.draft.value

        binding.tvStep.text = getString(R.string.search_profile_step, 3, 3)
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        // TODO(S1-13) : envoyer le brouillon avec PUT /me/tenant-profile avant de quitter l'écran.
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_profile_3_to_home)
        }

        bind(binding.sliderImportanceBudget, binding.tvBudgetLevel, Criterion.BUDGET, draft.importanceBudget)
        bind(binding.sliderImportanceDistance, binding.tvDistanceLevel, Criterion.DISTANCE, draft.importanceDistance)
        bind(binding.sliderImportanceAmenities, binding.tvAmenitiesLevel, Criterion.AMENITIES, draft.importanceAmenities)
        bind(binding.sliderImportanceRating, binding.tvRatingLevel, Criterion.RATING, draft.importanceRating)
    }

    private fun bind(slider: Slider, label: TextView, criterion: Criterion, initial: Int) {
        slider.value = initial.toFloat()
        label.text = levelText(initial)
        slider.addOnChangeListener { _, value, fromUser ->
            label.text = levelText(value.toInt())
            if (fromUser) viewModel.setImportance(criterion, value.toInt())
        }
    }

    private fun levelText(value: Int): String {
        val level = when (importanceLevel(value)) {
            ImportanceLevel.VERY_IMPORTANT -> R.string.search_importance_very
            ImportanceLevel.IMPORTANT -> R.string.search_importance_important
            ImportanceLevel.SECONDARY -> R.string.search_importance_secondary
        }
        return getString(R.string.search_importance_value, getString(level), value)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
