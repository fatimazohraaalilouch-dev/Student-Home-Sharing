package ma.shs.app.ui.profile

import java.text.Normalizer

/** Types de logement (ListingType dans docs/openapi.yaml). « Colocation » = SHARED_ROOM (décision 8). */
enum class ListingType(val code: String) {
    ROOM("ROOM"),
    STUDIO("STUDIO"),
    APARTMENT("APARTMENT"),
    SHARED_ROOM("SHARED_ROOM")
}

/** Les 6 équipements du catalogue (décision 9). */
enum class Amenity(val code: String) {
    WIFI("WIFI"),
    HOT_WATER("HOT_WATER"),
    KITCHEN("KITCHEN"),
    WASHER("WASHER"),
    DESK("DESK"),
    AIR_CONDITIONING("AIR_CONDITIONING")
}

enum class Criterion { BUDGET, DISTANCE, AMENITIES, RATING }

enum class ImportanceLevel { VERY_IMPORTANT, IMPORTANT, SECONDARY }

/** Établissement affiché à l'écran Profil 1. [id] sera l'UUID renvoyé par GET /establishments (S1-13). */
data class EstablishmentUi(val id: String, val name: String, val description: String)

/**
 * Brouillon du profil locataire, partagé par les 3 écrans. Les valeurs par défaut des importances
 * sont celles de la table tenant_profile (90, 75, 70, 40). Aucun type ni équipement n'est coché au départ,
 * pour ne pas filtrer les résultats sans que l'étudiant l'ait demandé.
 */
data class TenantProfileDraft(
    val establishment: EstablishmentUi? = null,
    val budgetMax: Int = DEFAULT_BUDGET_MAX,
    val maxDistanceKm: Float = DEFAULT_DISTANCE_KM,
    val preferredTypes: Set<ListingType> = emptySet(),
    val requiredAmenities: Set<Amenity> = emptySet(),
    val importanceBudget: Int = 90,
    val importanceDistance: Int = 75,
    val importanceAmenities: Int = 70,
    val importanceRating: Int = 40
) {
    companion object {
        const val BUDGET_MIN = 500
        const val BUDGET_MAX = 3000
        const val DEFAULT_BUDGET_MAX = 2000
        const val DISTANCE_MIN_KM = 0.5f
        const val DISTANCE_MAX_KM = 5f
        const val DEFAULT_DISTANCE_KM = 3f
    }
}

/** Niveau affiché à côté d'un curseur d'importance : 80 et plus très important, 50 à 79 important, sinon secondaire. */
fun importanceLevel(value: Int): ImportanceLevel = when {
    value >= 80 -> ImportanceLevel.VERY_IMPORTANT
    value >= 50 -> ImportanceLevel.IMPORTANT
    else -> ImportanceLevel.SECONDARY
}

/** Minuscules sans accents, pour chercher « hoceima » et trouver « Hoceïma ». */
fun String.foldForSearch(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD).replace(Regex("\\p{Mn}+"), "").lowercase()
