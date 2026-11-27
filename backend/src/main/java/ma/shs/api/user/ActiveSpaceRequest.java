package ma.shs.api.user;

import jakarta.validation.constraints.NotNull;

/** Corps de PUT /me/active-space. Une valeur autre que TENANT ou OWNER donne un 400. */
public record ActiveSpaceRequest(@NotNull(message = "obligatoire") Space space) {
}
