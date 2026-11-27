package ma.shs.app.ui.system

import android.os.Bundle
import androidx.fragment.app.Fragment

/** Choix de l'utilisateur sur un écran d'autorisation. L'écran appelant lance ensuite la demande système. */
object PermissionResult {
    const val KEY = "permission_result"
    const val PERMISSION = "permission"
    const val CHOICE = "choice"

    const val CAMERA = "camera"
    const val GALLERY = "gallery"
    const val LOCATION = "location"

    const val ALLOW = "allow"
    const val ALLOW_ONCE = "allow_once"
    const val DENY = "deny"

    fun send(fragment: Fragment, permission: String, choice: String) {
        fragment.parentFragmentManager.setFragmentResult(
            KEY,
            Bundle().apply {
                putString(PERMISSION, permission)
                putString(CHOICE, choice)
            }
        )
    }
}