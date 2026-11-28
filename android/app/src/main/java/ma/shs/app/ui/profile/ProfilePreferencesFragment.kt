package ma.shs.app.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import ma.shs.app.R
import ma.shs.app.databinding.FragmentProfilePreferencesBinding
import java.text.NumberFormat

/** Profil locataire 2/3 : budget, distance, types de logement, équipements. */
class ProfilePreferencesFragment : Fragment(R.layout.fragment_profile_preferences) {

    private val viewModel: TenantProfileViewModel by navGraphViewModels(R.id.nav_tenant_profile)
    private var _binding: FragmentProfilePreferencesBinding? = null
    private val binding get() = _binding!!

    private val typeLabels = linkedMapOf(
        ListingType.SHARED_ROOM to R.string.search_type_shared_room,
        ListingType.STUDIO to R.string.search_type_studio,
        ListingType.ROOM to R.string.search_type_room,
        ListingType.APARTMENT to R.string.search_type_apartment
    )

    private val amenityLabels = linkedMapOf(
        Amenity.WIFI to R.string.search_amenity_wifi,
        Amenity.HOT_WATER to R.string.search_amenity_hot_water,
        Amenity.KITCHEN to R.string.search_amenity_kitchen,
        Amenity.WASHER to R.string.search_amenity_washer,
        Amenity.DESK to R.string.search_amenity_desk,
        Amenity.AIR_CONDITIONING to R.string.search_amenity_air_conditioning
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentProfilePreferencesBinding.bind(view)
        val draft = viewModel.draft.value

        binding.tvStep.text = getString(R.string.search_profile_step, 2, 3)
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_profile_2_to_3)
        }

        binding.sliderBudget.value = draft.budgetMax.toFloat()
        showBudget(draft.budgetMax.toFloat())
        binding.sliderBudget.addOnChangeListener { _, value, fromUser ->
            showBudget(value)
            if (fromUser) viewModel.setBudgetMax(value.toInt())
        }

        binding.sliderDistance.value = draft.maxDistanceKm
        showDistance(draft.maxDistanceKm)
        binding.sliderDistance.addOnChangeListener { _, value, fromUser ->
            showDistance(value)
            if (fromUser) viewModel.setMaxDistance(value)
        }

        fill(binding.cgTypes, typeLabels, draft.preferredTypes, viewModel::toggleType)
        fill(binding.cgAmenities, amenityLabels, draft.requiredAmenities, viewModel::toggleAmenity)
    }

    private fun showBudget(value: Float) {
        binding.tvBudgetValue.text = getString(
            R.string.search_profile_2_budget_value, NumberFormat.getIntegerInstance().format(value.toInt())
        )
    }

    private fun showDistance(value: Float) {
        val format = NumberFormat.getNumberInstance().apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 1
        }
        binding.tvDistanceValue.text = getString(R.string.search_profile_2_distance_value, format.format(value))
    }

    /** Crée une puce par valeur ; une puce cochée est ajoutée au brouillon, décochée elle est retirée. */
    private fun <T> fill(
        group: ChipGroup,
        labels: Map<T, Int>,
        selected: Set<T>,
        onToggle: (T) -> Unit
    ) {
        group.removeAllViews()
        labels.forEach { (value, label) ->
            val chip = layoutInflater.inflate(R.layout.item_chip_filter, group, false) as Chip
            chip.setText(label)
            chip.isChecked = value in selected
            chip.setOnCheckedChangeListener { _, _ -> onToggle(value) }
            group.addView(chip)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
