package ma.shs.app.ui.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ma.shs.app.common.BaseViewModel

/**
 * État des 3 écrans du profil locataire (partagé par le graphe nav_tenant_profile).
 * Aucun appel réseau dans S1-10 : GET /establishments et PUT /me/tenant-profile sont branchés en S1-13.
 */
class TenantProfileViewModel : BaseViewModel() {

    private val _draft = MutableStateFlow(TenantProfileDraft())
    val draft: StateFlow<TenantProfileDraft> = _draft

    private var catalog: List<EstablishmentUi> = emptyList()
    private var query: String = ""

    private val _results = MutableStateFlow<List<EstablishmentUi>>(emptyList())
    val results: StateFlow<List<EstablishmentUi>> = _results

    /**
     * Fournit la liste des établissements. Rappelée à chaque affichage de l'écran : après un changement de langue
     * le ViewModel survit, mais les textes doivent être ceux de la nouvelle langue (l'établissement choisi est retrouvé par son id).
     */
    fun setCatalog(list: List<EstablishmentUi>) {
        catalog = list
        val selected = _draft.value.establishment
        if (selected != null) {
            list.firstOrNull { it.id == selected.id }?.let { _draft.value = _draft.value.copy(establishment = it) }
        }
        refreshResults()
    }

    fun search(text: String) {
        query = text
        refreshResults()
    }

    private fun refreshResults() {
        val q = query.trim().foldForSearch()
        _results.value = if (q.isEmpty()) catalog else catalog.filter {
            it.name.foldForSearch().contains(q) || it.description.foldForSearch().contains(q)
        }
    }

    fun selectEstablishment(establishment: EstablishmentUi) {
        _draft.value = _draft.value.copy(establishment = establishment)
    }

    fun setBudgetMax(value: Int) {
        _draft.value = _draft.value.copy(
            budgetMax = value.coerceIn(TenantProfileDraft.BUDGET_MIN, TenantProfileDraft.BUDGET_MAX)
        )
    }

    fun setMaxDistance(km: Float) {
        _draft.value = _draft.value.copy(
            maxDistanceKm = km.coerceIn(TenantProfileDraft.DISTANCE_MIN_KM, TenantProfileDraft.DISTANCE_MAX_KM)
        )
    }

    fun toggleType(type: ListingType) {
        val current = _draft.value.preferredTypes
        _draft.value = _draft.value.copy(preferredTypes = if (type in current) current - type else current + type)
    }

    fun toggleAmenity(amenity: Amenity) {
        val current = _draft.value.requiredAmenities
        _draft.value = _draft.value.copy(requiredAmenities = if (amenity in current) current - amenity else current + amenity)
    }

    fun setImportance(criterion: Criterion, value: Int) {
        val v = value.coerceIn(0, 100)
        val d = _draft.value
        _draft.value = when (criterion) {
            Criterion.BUDGET -> d.copy(importanceBudget = v)
            Criterion.DISTANCE -> d.copy(importanceDistance = v)
            Criterion.AMENITIES -> d.copy(importanceAmenities = v)
            Criterion.RATING -> d.copy(importanceRating = v)
        }
    }
}
