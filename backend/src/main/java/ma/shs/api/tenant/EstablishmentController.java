package ma.shs.api.tenant;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * GET /establishments?query= : recherche par nom, université ou ville, 20 résultats au plus.
 * La colonne geography n'est pas mappée avec JPA : latitude et longitude viennent du SQL.
 */
@RestController
@RequestMapping("/establishments")
public class EstablishmentController {

    private static final int LIMIT = 20;

    private final NamedParameterJdbcTemplate jdbc;

    public EstablishmentController(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping
    public List<EstablishmentResponse> search(@RequestParam(required = false) String query) {
        String text = query == null ? "" : query.trim();
        String pattern = "%" + text.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%";
        return jdbc.query(
                "SELECT id, name, university_name, address_text, city, "
                        + "ST_Y(location::geometry) AS latitude, ST_X(location::geometry) AS longitude "
                        + "FROM establishment "
                        + "WHERE name ILIKE :p OR university_name ILIKE :p OR city ILIKE :p "
                        + "ORDER BY name LIMIT " + LIMIT,
                Map.of("p", pattern),
                (rs, i) -> new EstablishmentResponse(
                        rs.getObject("id", UUID.class),
                        rs.getString("name"),
                        rs.getString("university_name"),
                        rs.getString("address_text"),
                        rs.getString("city"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")));
    }
}
