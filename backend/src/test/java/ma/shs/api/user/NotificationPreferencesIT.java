package ma.shs.api.user;

import static ma.shs.api.user.TestUsers.loggedAs;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

/** Tests de GET/PUT /me/notification-preferences (décision 17 : 5 clés et valeurs par défaut). */
class NotificationPreferencesIT extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    JdbcTemplate jdbc;

    @Test
    void sansLigneEnBase_renvoieLes5ClesAvecLesValeursParDefaut() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(get("/me/notification-preferences").with(loggedAs(id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].key").value("messages"))
                .andExpect(jsonPath("$[0].enabled").value(true))
                .andExpect(jsonPath("$[1].key").value("booking_updates"))
                .andExpect(jsonPath("$[1].enabled").value(true))
                .andExpect(jsonPath("$[2].key").value("listing_updates"))
                .andExpect(jsonPath("$[2].enabled").value(true))
                .andExpect(jsonPath("$[3].key").value("copilot_tips"))
                .andExpect(jsonPath("$[3].enabled").value(false))
                .andExpect(jsonPath("$[4].key").value("news"))
                .andExpect(jsonPath("$[4].enabled").value(false));
    }

    @Test
    void put_modifieLesClesRecuesEtGardeLesAutres() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(put("/me/notification-preferences").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"key\":\"messages\",\"enabled\":false},"
                                + "{\"key\":\"news\",\"enabled\":true}]"))
                .andExpect(status().isOk());

        // Deuxième PUT sur la même clé : mise à jour, pas de doublon.
        mockMvc.perform(put("/me/notification-preferences").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"key\":\"news\",\"enabled\":false}]"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/me/notification-preferences").with(loggedAs(id)))
                .andExpect(jsonPath("$[0].key").value("messages"))
                .andExpect(jsonPath("$[0].enabled").value(false))
                .andExpect(jsonPath("$[1].enabled").value(true))
                .andExpect(jsonPath("$[4].key").value("news"))
                .andExpect(jsonPath("$[4].enabled").value(false));
    }

    @Test
    void put_cleInconnue_renvoie400ValidationError() throws Exception {
        UUID id = TestUsers.create(jdbc);
        mockMvc.perform(put("/me/notification-preferences").with(loggedAs(id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"key\":\"promos\",\"enabled\":true}]"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details['[0].key']").exists());
    }
}
