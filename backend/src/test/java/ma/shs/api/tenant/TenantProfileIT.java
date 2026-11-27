package ma.shs.api.tenant;

import static ma.shs.api.user.TestUsers.loggedAs;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import ma.shs.api.IntegrationTestBase;
import ma.shs.api.user.TestUsers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

/** Tests de GET/PUT /me/tenant-profile (budgetMin facultatif, importances 0 à 100). */
class TenantProfileIT extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JdbcTemplate jdbc;

    private UUID anEstablishment() {
        return jdbc.queryForObject("SELECT id FROM establishment ORDER BY name LIMIT 1", UUID.class);
    }

    private String body(UUID establishment, String extra) {
        return "{\"establishmentId\":\"" + establishment + "\",\"budgetMax\":1500,\"maxDistanceKm\":3"
                + (extra.isEmpty() ? "" : "," + extra) + "}";
    }

    @Test
    void get_sansProfil_renvoie404() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(get("/me/tenant-profile").with(loggedAs(id)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void put_puisGet_renvoientLesMemesValeurs() throws Exception {
        UUID id = TestUsers.create(jdbc);
        UUID est = anEstablishment();
        String json = body(est, "\"preferredTypes\":[\"STUDIO\",\"ROOM\"],"
                + "\"requiredAmenities\":[\"WIFI\",\"DESK\"],"
                + "\"importanceBudget\":80,\"importanceDistance\":60,"
                + "\"importanceAmenities\":50,\"importanceRating\":30");

        mockMvc.perform(put("/me/tenant-profile").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/me/tenant-profile").with(loggedAs(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.establishmentId").value(est.toString()))
                .andExpect(jsonPath("$.budgetMin").doesNotExist())
                .andExpect(jsonPath("$.budgetMax").value(1500.0))
                .andExpect(jsonPath("$.maxDistanceKm").value(3.0))
                .andExpect(jsonPath("$.preferredTypes[0]").value("STUDIO"))
                .andExpect(jsonPath("$.requiredAmenities[1]").value("DESK"))
                .andExpect(jsonPath("$.importanceBudget").value(80))
                .andExpect(jsonPath("$.importanceRating").value(30));
    }

    @Test
    void put_sansImportances_prendLesValeursParDefautDeLaBase() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(put("/me/tenant-profile").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON).content(body(anEstablishment(), "")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.importanceBudget").value(90))
                .andExpect(jsonPath("$.importanceDistance").value(75))
                .andExpect(jsonPath("$.importanceAmenities").value(70))
                .andExpect(jsonPath("$.importanceRating").value(40))
                .andExpect(jsonPath("$.preferredTypes.length()").value(0));
    }

    @Test
    void put_deuxFois_metAJourLeMemeProfil() throws Exception {
        UUID id = TestUsers.create(jdbc);
        UUID est = anEstablishment();
        mockMvc.perform(put("/me/tenant-profile").with(loggedAs(id))
                .contentType(MediaType.APPLICATION_JSON).content(body(est, "")));
        mockMvc.perform(put("/me/tenant-profile").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(est, "\"budgetMin\":500")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budgetMin").value(500.0));
    }

    @Test
    void put_importanceHorsBornes_renvoie400AvecLeChamp() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(put("/me/tenant-profile").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(anEstablishment(), "\"importanceBudget\":150")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.importanceBudget").exists());
    }

    @Test
    void put_budgetMinSuperieurAuBudgetMax_renvoie400() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(put("/me/tenant-profile").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(anEstablishment(), "\"budgetMin\":2000")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.budgetMin").exists());
    }

    @Test
    void put_champsObligatoiresAbsents_renvoie400() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(put("/me/tenant-profile").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.establishmentId").exists())
                .andExpect(jsonPath("$.details.budgetMax").exists())
                .andExpect(jsonPath("$.details.maxDistanceKm").exists());
    }

    @Test
    void put_etablissementInconnuOuValeursHorsCatalogue_renvoie400() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(put("/me/tenant-profile").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body(UUID.randomUUID(), "\"preferredTypes\":[\"VILLA\"],"
                                + "\"requiredAmenities\":[\"PISCINE\"]")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.establishmentId").exists())
                .andExpect(jsonPath("$.details.preferredTypes").exists())
                .andExpect(jsonPath("$.details.requiredAmenities").exists());
    }
}
