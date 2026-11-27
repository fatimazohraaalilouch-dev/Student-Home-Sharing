package ma.shs.api.user;

import static ma.shs.api.user.TestUsers.loggedAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import ma.shs.api.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

/** Tests de GET/PATCH/DELETE /me, PUT /me/active-space et POST /me/spaces (S1-06). */
class UserControllerIT extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JdbcTemplate jdbc;

    @Test
    void getMe_renvoieLeProfilEtLesEspaces() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(get("/me").with(loggedAs(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.fullName").value("Salma Test"))
                .andExpect(jsonPath("$.preferredLanguage").value("fr"))
                .andExpect(jsonPath("$.activeSpace").value("TENANT"))
                .andExpect(jsonPath("$.spaces[0]").value("TENANT"));
    }

    @Test
    void getMe_utilisateurInconnu_renvoie404() throws Exception {
        mockMvc.perform(get("/me").with(loggedAs(UUID.randomUUID())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void patchMe_modifieSeulementLesChampsEnvoyes() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(patch("/me").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"preferredLanguage\":\"ar\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preferredLanguage").value("ar"))
                .andExpect(jsonPath("$.fullName").value("Salma Test"));
    }

    @Test
    void patchMe_langueInconnue_renvoie400AvecDetailParChamp() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(patch("/me").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"preferredLanguage\":\"es\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.preferredLanguage").exists());
    }

    @Test
    void patchMe_nomVide_renvoie400() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(patch("/me").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"   \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.fullName").exists());
    }

    @Test
    void deleteMe_marqueLeCompteSupprimeEtRevoqueLesJetons() throws Exception {
        UUID id = TestUsers.create(jdbc);
        jdbc.update("INSERT INTO refresh_token (user_id, token_hash, expires_at) "
                + "VALUES (?, 'hash-factice', now() + interval '1 day')", id);

        mockMvc.perform(delete("/me").with(loggedAs(id)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/me").with(loggedAs(id)))
                .andExpect(status().isNotFound());
        assertNotNull(jdbc.queryForObject(
                "SELECT deleted_at FROM app_user WHERE id = ?", java.sql.Timestamp.class, id));
        assertEquals(Boolean.FALSE, jdbc.queryForObject(
                "SELECT is_active FROM app_user WHERE id = ?", Boolean.class, id));
        assertNotNull(jdbc.queryForObject(
                "SELECT revoked_at FROM refresh_token WHERE user_id = ?", java.sql.Timestamp.class, id));
    }

    @Test
    void activeSpace_proprietaireSansEspaceActive_renvoie403() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(put("/me/active-space").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"space\":\"OWNER\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("FORBIDDEN"));
    }

    @Test
    void activerEspaceProprietaire_puisBasculer() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(post("/me/spaces").with(loggedAs(id)))
                .andExpect(status().isCreated());
        // Deuxième appel : sans effet, même réponse.
        mockMvc.perform(post("/me/spaces").with(loggedAs(id)))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/me/active-space").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"space\":\"OWNER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeSpace").value("OWNER"))
                .andExpect(jsonPath("$.spaces.length()").value(2));
    }

    @Test
    void activeSpace_valeurInconnue_renvoie400() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(put("/me/active-space").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"space\":\"ADMIN\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
