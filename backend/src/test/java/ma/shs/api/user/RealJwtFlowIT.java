package ma.shs.api.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import java.util.UUID;
import ma.shs.api.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Parcours avec un vrai jeton : inscription (S1-05), puis routes /me (S1-06).
 * Vérifie que les deux lots sont compatibles (identifiant du jeton, espace TENANT à l'inscription).
 */
class RealJwtFlowIT extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    @Test
    void inscriptionPuisProfilEtEspaces() throws Exception {
        String email = "flow-" + UUID.randomUUID() + "@example.com";
        String body = mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"Motdepasse123\","
                                + "\"fullName\":\"Flow Test\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String bearer = "Bearer " + JsonPath.read(body, "$.accessToken");

        mockMvc.perform(get("/me").header("Authorization", bearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.activeSpace").value("TENANT"))
                .andExpect(jsonPath("$.spaces[0]").value("TENANT"));

        mockMvc.perform(post("/me/spaces").header("Authorization", bearer))
                .andExpect(status().isCreated());
        mockMvc.perform(put("/me/active-space").header("Authorization", bearer)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"space\":\"OWNER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeSpace").value("OWNER"));
    }

    @Test
    void compteSupprime_nePeutPlusSeConnecter() throws Exception {
        String email = "gone-" + UUID.randomUUID() + "@example.com";
        String creds = "{\"email\":\"" + email + "\",\"password\":\"Motdepasse123\"";
        String body = mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(creds + ",\"fullName\":\"Gone Test\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String bearer = "Bearer " + JsonPath.read(body, "$.accessToken");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/me")
                        .header("Authorization", bearer))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(creds + "}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
    }
}
