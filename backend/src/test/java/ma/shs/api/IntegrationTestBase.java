package ma.shs.api;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Base commune des tests d'intégration (tous lots) : démarre toute l'application sur une vraie
 * base PostgreSQL + PostGIS. Flyway applique V1, V2... : chaque PR valide donc aussi les migrations.
 * Un seul conteneur est démarré pour toute la JVM de test (plus rapide, et sûr avec le cache de
 * contextes de Spring). Nommer les classes filles *IT (exécutées par failsafe : mvn verify).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class IntegrationTestBase {

    @ServiceConnection
    static final PostgreSQLContainer<?> DB = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:16-3.4").asCompatibleSubstituteFor("postgres"));

    static {
        DB.start();
    }
}
