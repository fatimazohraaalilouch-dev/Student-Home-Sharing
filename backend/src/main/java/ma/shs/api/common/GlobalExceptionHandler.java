package ma.shs.api.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Format d'erreur unique {code, message, details} pour tous les lots. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApi(ApiException ex) {
        if (ex.status().is5xxServerError()) {
            log.error("ApiException (status={}, code={})", ex.status().value(), ex.code());
        }
        return build(ex.status(), ex.code(), ex.getMessage(), ex.details());
    }

    /** @Valid sur un corps ou un DTO : details = {champ: message}. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleBodyValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            details.putIfAbsent(fe.getField(), messageOrDefault(fe.getDefaultMessage()));
        }
        for (ObjectError oe : ex.getBindingResult().getGlobalErrors()) {
            details.putIfAbsent(oe.getObjectName(), messageOrDefault(oe.getDefaultMessage()));
        }
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Données invalides", details);
    }

    /** @Validated sur des paramètres (@RequestParam, @PathVariable). */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            String path = v.getPropertyPath().toString();
            String field = path.substring(path.lastIndexOf('.') + 1);
            details.putIfAbsent(field, messageOrDefault(v.getMessage()));
        }
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Données invalides", details);
    }

    /** JSON mal formé, type incorrect dans le corps, énumération inconnue... */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR",
                "Corps de la requête illisible ou invalide", Map.of());
    }

    /** Ex. : /listings/abc alors qu'un UUID est attendu. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Données invalides",
                Map.of(ex.getName(), "Valeur invalide"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "FORBIDDEN", "Accès refusé", Map.of());
    }

    /**
     * Filet de sécurité. Les erreurs HTTP standard de Spring (404 route inconnue, 405, 415,
     * paramètre manquant...) implémentent ErrorResponse : on garde leur statut au lieu de
     * les transformer en 500. Tout le reste est une vraie erreur serveur : détail dans les
     * logs, jamais dans la réponse.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOther(Exception ex) {
        if (ex instanceof ErrorResponse er) {
            HttpStatusCode status = er.getStatusCode();
            if (status.value() == 404) {
                return build(status, "NOT_FOUND", "Ressource introuvable", Map.of());
            }
            if (status.value() == 403) {
                return build(status, "FORBIDDEN", "Accès refusé", Map.of());
            }
            if (status.is4xxClientError()) {
                return build(status, "VALIDATION_ERROR", "Requête invalide", Map.of());
            }
        }
        log.error("Erreur non gérée (type={})", ex.getClass().getName());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR",
                "Erreur interne du serveur", Map.of());
    }

    private static ResponseEntity<ApiError> build(HttpStatusCode status, String code,
                                                  String message, Map<String, Object> details) {
        return ResponseEntity.status(status).body(new ApiError(code, message, details));
    }

    private static String messageOrDefault(String message) {
        return message != null ? message : "Valeur invalide";
    }
}