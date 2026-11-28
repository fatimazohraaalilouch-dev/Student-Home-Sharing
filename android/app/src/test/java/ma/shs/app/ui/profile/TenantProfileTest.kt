package ma.shs.app.ui.profile

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TenantProfileTest {

    private val ensah = EstablishmentUi("1", "ENSA Al Hoceïma", "École Nationale des Sciences Appliquées\nAjdir, Al Hoceïma")
    private val fst = EstablishmentUi("2", "Faculté des Sciences et Techniques", "Al Hoceïma · Université Abdelmalek Essaâdi")
    private val ispits = EstablishmentUi("3", "ISPITS Al Hoceïma", "Al Hoceïma · Ministère de la Santé")

    @Test
    fun valeursParDefaut_correspondentALaBase() {
        val d = TenantProfileViewModel().draft.value
        assertEquals(90, d.importanceBudget)
        assertEquals(75, d.importanceDistance)
        assertEquals(70, d.importanceAmenities)
        assertEquals(40, d.importanceRating)
        assertEquals(2000, d.budgetMax)
        assertEquals(3f, d.maxDistanceKm, 0f)
        assertTrue(d.preferredTypes.isEmpty())
        assertTrue(d.requiredAmenities.isEmpty())
        assertNull(d.establishment)
    }

    @Test
    fun niveauxDImportance_commeSurLaMaquette() {
        assertEquals(ImportanceLevel.VERY_IMPORTANT, importanceLevel(90))
        assertEquals(ImportanceLevel.IMPORTANT, importanceLevel(75))
        assertEquals(ImportanceLevel.IMPORTANT, importanceLevel(70))
        assertEquals(ImportanceLevel.SECONDARY, importanceLevel(40))
    }

    @Test
    fun niveauxDImportance_aux_bornes() {
        assertEquals(ImportanceLevel.VERY_IMPORTANT, importanceLevel(80))
        assertEquals(ImportanceLevel.IMPORTANT, importanceLevel(79))
        assertEquals(ImportanceLevel.IMPORTANT, importanceLevel(50))
        assertEquals(ImportanceLevel.SECONDARY, importanceLevel(49))
        assertEquals(ImportanceLevel.SECONDARY, importanceLevel(0))
    }

    @Test
    fun importance_estLimiteEntre0Et100() {
        val vm = TenantProfileViewModel()
        vm.setImportance(Criterion.BUDGET, 150)
        vm.setImportance(Criterion.RATING, -5)
        assertEquals(100, vm.draft.value.importanceBudget)
        assertEquals(0, vm.draft.value.importanceRating)
    }

    @Test
    fun importance_modifieSeulementLeCritereDemande() {
        val vm = TenantProfileViewModel()
        vm.setImportance(Criterion.DISTANCE, 20)
        assertEquals(20, vm.draft.value.importanceDistance)
        assertEquals(90, vm.draft.value.importanceBudget)
        assertEquals(70, vm.draft.value.importanceAmenities)
    }

    @Test
    fun budgetEtDistance_sontBornes() {
        val vm = TenantProfileViewModel()
        vm.setBudgetMax(100)
        assertEquals(500, vm.draft.value.budgetMax)
        vm.setBudgetMax(9000)
        assertEquals(3000, vm.draft.value.budgetMax)
        vm.setMaxDistance(0f)
        assertEquals(0.5f, vm.draft.value.maxDistanceKm, 0f)
        vm.setMaxDistance(12f)
        assertEquals(5f, vm.draft.value.maxDistanceKm, 0f)
    }

    @Test
    fun typesEtEquipements_sAjoutentEtSeRetirent() {
        val vm = TenantProfileViewModel()
        vm.toggleType(ListingType.STUDIO)
        vm.toggleType(ListingType.SHARED_ROOM)
        vm.toggleType(ListingType.STUDIO)
        assertEquals(setOf(ListingType.SHARED_ROOM), vm.draft.value.preferredTypes)
        vm.toggleAmenity(Amenity.WIFI)
        vm.toggleAmenity(Amenity.DESK)
        assertEquals(setOf(Amenity.WIFI, Amenity.DESK), vm.draft.value.requiredAmenities)
        vm.toggleAmenity(Amenity.WIFI)
        assertFalse(Amenity.WIFI in vm.draft.value.requiredAmenities)
    }

    @Test
    fun catalogue_codesCorrespondentALApi() {
        assertEquals(listOf("ROOM", "STUDIO", "APARTMENT", "SHARED_ROOM"), ListingType.values().map { it.code })
        assertEquals(
            listOf("WIFI", "HOT_WATER", "KITCHEN", "WASHER", "DESK", "AIR_CONDITIONING"),
            Amenity.values().map { it.code }
        )
    }

    @Test
    fun recherche_ignoreAccentsEtCasse() {
        val vm = TenantProfileViewModel()
        vm.setCatalog(listOf(ensah, fst, ispits))
        vm.search("hoceima")
        assertEquals(listOf(ensah, fst, ispits), vm.results.value)
        vm.search("ENSA")
        assertEquals(listOf(ensah), vm.results.value)
        vm.search("faculte")
        assertEquals(listOf(fst), vm.results.value)
    }

    @Test
    fun recherche_vide_renvoieToutEtSansResultat_renvoieUneListeVide() {
        val vm = TenantProfileViewModel()
        vm.setCatalog(listOf(ensah, fst))
        vm.search("   ")
        assertEquals(2, vm.results.value.size)
        vm.search("zzzz")
        assertTrue(vm.results.value.isEmpty())
    }

    @Test
    fun catalogue_rechargeDansUneAutreLangue_gardeLaSelectionParId() {
        val vm = TenantProfileViewModel()
        vm.setCatalog(listOf(ensah, fst))
        vm.selectEstablishment(fst)
        val fstEn = EstablishmentUi("2", "Faculty of Science and Technology", "Al Hoceima")
        vm.setCatalog(listOf(ensah.copy(name = "ENSA Al Hoceima"), fstEn))
        assertEquals(fstEn, vm.draft.value.establishment)
        assertEquals("ENSA Al Hoceima", vm.results.value.first().name)
    }

    @Test
    fun selectionDEtablissement_estGardeeDansLeBrouillon() {
        val vm = TenantProfileViewModel()
        vm.selectEstablishment(fst)
        assertEquals("2", vm.draft.value.establishment?.id)
    }
}
