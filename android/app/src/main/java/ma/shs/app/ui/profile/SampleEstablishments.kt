package ma.shs.app.ui.profile

import android.content.Context
import ma.shs.app.R

/**
 * Établissements d'exemple (les 4 de la migration V3). Les identifiants ne sont pas ceux de la base :
 * TODO(S1-13) remplacer par GET /establishments?query= (UUID réels).
 */
object SampleEstablishments {
    fun all(context: Context): List<EstablishmentUi> = listOf(
        EstablishmentUi(
            "sample-ensah",
            context.getString(R.string.search_sample_ensah_name),
            context.getString(R.string.search_sample_ensah_desc)
        ),
        EstablishmentUi(
            "sample-fst",
            context.getString(R.string.search_sample_fst_name),
            context.getString(R.string.search_sample_fst_desc)
        ),
        EstablishmentUi(
            "sample-campus",
            context.getString(R.string.search_sample_campus_name),
            context.getString(R.string.search_sample_campus_desc)
        ),
        EstablishmentUi(
            "sample-ispits",
            context.getString(R.string.search_sample_ispits_name),
            context.getString(R.string.search_sample_ispits_desc)
        )
    )
}
