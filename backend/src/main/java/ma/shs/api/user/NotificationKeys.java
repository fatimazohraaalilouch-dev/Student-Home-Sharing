package ma.shs.api.user;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Clés de notification et valeurs par défaut (décision 17 de docs/DECISIONS.md).
 * Quand aucune ligne n'existe en base, l'API renvoie ces valeurs.
 */
public final class NotificationKeys {

    /** Ordre = ordre des interrupteurs de l'écran Réglages > Notifications. */
    public static final Map<String, Boolean> DEFAULTS = defaults();

    private NotificationKeys() {
    }

    private static Map<String, Boolean> defaults() {
        Map<String, Boolean> m = new LinkedHashMap<>();
        m.put("messages", true);
        m.put("booking_updates", true);
        m.put("listing_updates", true);
        m.put("copilot_tips", false);
        m.put("news", false);
        return Collections.unmodifiableMap(m);
    }
}
