package ma.shs.app.ui.system

import android.os.Bundle
import androidx.fragment.app.Fragment

/** Choix de l'utilisateur sur les écrans de compte. L'écran appelant déclenche ensuite l'action réelle. */
object AccountResult {
    const val KEY = "account_result"
    const val ACTION = "action"

    const val LOGOUT = "logout"
    const val STAY = "stay"
    const val DELETE = "delete"
    const val KEEP = "keep"
    const val HOME = "home"

    fun send(fragment: Fragment, action: String) {
        fragment.parentFragmentManager.setFragmentResult(
            KEY,
            Bundle().apply { putString(ACTION, action) }
        )
    }
}