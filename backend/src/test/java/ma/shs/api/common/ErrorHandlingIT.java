package ma.shs.api.common;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import ma.shs.api.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exemple de test d'intégration (S1-07) : vérifie le format d'erreur {code, message, details}.
 * Note : MockMvc ignore server.servlet.context-path, on appelle donc /me et non /api/v1/me.
 */
@Import(ErrorHandlingIT.ProbeController.class)
class ErrorHandlingIT extends IntegrationTestBase {

    @Autowired
    MockMvc mockMvc;

    private static Authentication connectedUser() {
        return new UsernamePasswordAuthenticationToken(UUID.randomUUID(), null, List.of());
    }

    @Test
    void routeProtegeeSansJeton_renvoie401() throws Exception {
        mockMvc.perform(get("/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void corpsInvalide_renvoie400ValidationErrorAvecDetailsParChamp() throws Exception {
        mockMvc.perform(post("/test-support/validation")
                        .with(authentication(connectedUser()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"pas-un-email\",\"age\":-5}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.details.email").exists())
                .andExpect(jsonPath("$.details.age").exists());
    }

    @Test
    void uuidInvalideDansLUrl_renvoie400ValidationError() throws Exception {
        mockMvc.perform(get("/test-support/typed/pas-un-uuid")
                        .with(authentication(connectedUser())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.id").exists());
    }

    @Test
    void apiExceptionNotFound_renvoie404AuBonFormat() throws Exception {
        mockMvc.perform(get("/test-support/not-found")
                        .with(authentication(connectedUser())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Annonce introuvable"));
    }

    @Test
    void routeInconnue_renvoie404NotFoundEtPasUne500() throws Exception {
        mockMvc.perform(get("/route-qui-nexiste-pas")
                        .with(authentication(connectedUser())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void exceptionInattendue_renvoie500SansDetailTechnique() throws Exception {
        mockMvc.perform(get("/test-support/boom")
                        .with(authentication(connectedUser())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("SERVER_ERROR"))
                .andExpect(content().string(not(containsString("detail-technique-secret"))));
    }

    /** Contrôleur présent uniquement dans les tests : sert à provoquer chaque type d'erreur. */
    @RestController
    @RequestMapping("/test-support")
    static class ProbeController {

        record Payload(@NotBlank @Email String email, @Min(0) int age) {
        }

        @PostMapping("/validation")
        void validate(@Valid @RequestBody Payload payload) {
        }

        @GetMapping("/typed/{id}")
        void typed(@PathVariable UUID id) {
        }

        @GetMapping("/not-found")
        void notFound() {
            throw ApiException.notFound("Annonce introuvable");
        }

        @GetMapping("/boom")
        void boom() {
            throw new IllegalStateException("detail-technique-secret");
        }
    }
}
