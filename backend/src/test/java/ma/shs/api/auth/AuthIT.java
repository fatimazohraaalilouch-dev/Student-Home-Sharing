package ma.shs.api.auth;

import com.jayway.jsonpath.JsonPath;
import ma.shs.api.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthIT extends IntegrationTestBase {

    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;

    private String json(String email, String password) {
        return "{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"fullName\":\"Test User\"}";
    }

    private String newEmail() {
        return "user-" + UUID.randomUUID() + "@example.com";
    }

    /** Inscrit un utilisateur et renvoie le refreshToken reçu. */
    private String registerAndGetRefresh(String email) throws Exception {
        String body = mvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(json(email, "Motdepasse123")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.refreshToken");
    }

    @Test
    void login_succeeds_and_returns_tokens() throws Exception {
        String email = newEmail();
        registerAndGetRefresh(email);
        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json(email, "Motdepasse123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void login_with_wrong_password_returns_401_invalid_credentials() throws Exception {
        String email = newEmail();
        registerAndGetRefresh(email);
        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json(email, "mauvais-mot-de-passe")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
    }

    @Test
    void refresh_with_expired_token_returns_401_session_expired() throws Exception {
        String email = newEmail();
        String refresh = registerAndGetRefresh(email);
        // On force l'expiration du jeton de CET utilisateur uniquement
        jdbc.update("UPDATE refresh_token SET expires_at = now() - interval '1 day' "
                + "WHERE user_id = (SELECT id FROM app_user WHERE email = ?)", email);
        mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refresh + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("SESSION_EXPIRED"));
    }

    @Test
    void refresh_rotates_and_old_token_is_refused() throws Exception {
        String refresh = registerAndGetRefresh(newEmail());
        String payload = "{\"refreshToken\":\"" + refresh + "\"}";
        mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isOk());
        mvc.perform(post("/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protected_route_without_token_returns_401() throws Exception {
        mvc.perform(get("/me")).andExpect(status().isUnauthorized());
    }
}