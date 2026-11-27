package ma.shs.api.user;

import jakarta.validation.Valid;
import java.util.List;
import ma.shs.api.common.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Routes /me (tag « me » de docs/openapi.yaml). L'identifiant de l'utilisateur vient du jeton
 * (voir {@link CurrentUser}).
 */
@RestController
@RequestMapping("/me")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public UserResponse get(Authentication auth) {
        return service.get(CurrentUser.id(auth));
    }

    @PatchMapping
    public UserResponse update(Authentication auth,
                               @Valid @RequestBody UserUpdateRequest request) {
        return service.update(CurrentUser.id(auth), request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication auth) {
        service.delete(CurrentUser.id(auth));
    }

    @PutMapping("/active-space")
    public UserResponse changeActiveSpace(Authentication auth,
                                          @Valid @RequestBody ActiveSpaceRequest request) {
        return service.changeActiveSpace(CurrentUser.id(auth), request.space());
    }

    @PostMapping("/spaces")
    @ResponseStatus(HttpStatus.CREATED)
    public void enableOwnerSpace(Authentication auth) {
        service.enableOwnerSpace(CurrentUser.id(auth));
    }

    @GetMapping("/notification-preferences")
    public List<NotificationPreferenceDto> notificationPreferences(
            Authentication auth) {
        return service.notificationPreferences(CurrentUser.id(auth));
    }

    @PutMapping("/notification-preferences")
    public List<NotificationPreferenceDto> updateNotificationPreferences(
            Authentication auth,
            @RequestBody List<NotificationPreferenceDto> preferences) {
        return service.updateNotificationPreferences(CurrentUser.id(auth), preferences);
    }
}
