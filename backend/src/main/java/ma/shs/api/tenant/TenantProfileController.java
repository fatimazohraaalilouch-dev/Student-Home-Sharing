package ma.shs.api.tenant;

import jakarta.validation.Valid;
import ma.shs.api.common.CurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** GET et PUT /me/tenant-profile (tag « tenant » de docs/openapi.yaml). */
@RestController
@RequestMapping("/me/tenant-profile")
public class TenantProfileController {

    private final TenantProfileService service;

    public TenantProfileController(TenantProfileService service) {
        this.service = service;
    }

    @GetMapping
    public TenantProfileDto get(Authentication auth) {
        return service.get(CurrentUser.id(auth));
    }

    @PutMapping
    public TenantProfileDto save(Authentication auth,
                                 @Valid @RequestBody TenantProfileDto request) {
        return service.save(CurrentUser.id(auth), request);
    }
}
