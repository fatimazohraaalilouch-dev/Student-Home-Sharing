package ma.shs.app.ui.common

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import ma.shs.app.R

/**
 * Met une carte de choix (Rôle, Langue) à l'état sélectionné ou non : contour épais teal, fond teinté,
 * pastille cochée. Les couleurs viennent du design system, donc elles suivent le mode sombre.
 */
fun MaterialCardView.applySelected(indicator: ImageView, selected: Boolean) {
    strokeColor = ContextCompat.getColor(
        context, if (selected) R.color.shs_teal_accent else R.color.shs_outline
    )
    strokeWidth = resources.getDimensionPixelSize(
        if (selected) R.dimen.stroke_option_selected else R.dimen.stroke_option_default
    )
    setCardBackgroundColor(
        ContextCompat.getColor(context, if (selected) R.color.shs_selected_container else R.color.shs_surface)
    )
    indicator.setImageResource(if (selected) R.drawable.ic_check_circle else R.drawable.ic_radio_unchecked)
    indicator.imageTintList = ColorStateList.valueOf(
        ContextCompat.getColor(context, if (selected) R.color.shs_teal_accent else R.color.shs_outline)
    )
    contentDescription = null
}
