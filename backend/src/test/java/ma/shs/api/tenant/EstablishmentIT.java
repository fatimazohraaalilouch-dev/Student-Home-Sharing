package ma.shs.api.tenant;

import static ma.shs.api.user.TestUsers.loggedAs;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import ma.shs.api.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

/** Tests de GET /establishments (données de départ de la migration V3). */
class EstablishmentIT extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Test
    void sansFiltre_renvoieLesEtablissementsDeDepart() throws Exception {
        mockMvc.perform(get("/establishments").with(loggedAs(UUID.randomUUID())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void recherche_parNomSansTenirCompteDeLaCasse() throws Exception {
        mockMvc.perform(get("/establishments").param("query", "ensah")
                        .with(loggedAs(UUID.randomUUID())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(org.hamcrest.Matchers.containsString("ENSAH")))
                .andExpect(jsonPath("$[0].latitude").value(org.hamcrest.Matchers.closeTo(35.172465, 0.0001)))
                .andExpect(jsonPath("$[0].longitude").value(org.hamcrest.Matchers.closeTo(-3.861684, 0.0001)));
    }

    @Test
    void recherche_sansResultat_renvoieUneListeVide() throws Exception {
        mockMvc.perform(get("/establishments").param("query", "zzzz-introuvable")
                        .with(loggedAs(UUID.randomUUID())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void recherche_caracteresSpeciauxSontTraitesCommeDuTexte() throws Exception {
        mockMvc.perform(get("/establishments").param("query", "%")
                        .with(loggedAs(UUID.randomUUID())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void sansJeton_renvoie401() throws Exception {
        mockMvc.perform(get("/establishments")).andExpect(status().isUnauthorized());
    }
}
