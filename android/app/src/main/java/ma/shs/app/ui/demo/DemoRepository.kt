package ma.shs.app.ui.demo

import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject

enum class DemoMode { CONTENT, EMPTY, NETWORK_ERROR }

interface DemoRepository {
    suspend fun items(mode: DemoMode): List<String>
}

/** Fausse source de données : simule un appel réseau de 800 ms. */
class FakeDemoRepository @Inject constructor() : DemoRepository {
    override suspend fun items(mode: DemoMode): List<String> {
        delay(800)
        return when (mode) {
            DemoMode.CONTENT -> listOf("Studio - Hay Riad", "Chambre - Agdal", "T2 - Maarif")
            DemoMode.EMPTY -> emptyList()
            DemoMode.NETWORK_ERROR -> throw IOException("réseau simulé")
        }
    }
}