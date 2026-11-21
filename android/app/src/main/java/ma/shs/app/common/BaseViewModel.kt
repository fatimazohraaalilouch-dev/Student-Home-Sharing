package ma.shs.app.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Modèle MVVM commun aux 3 lots : chaque ViewModel hérite de cette classe,
 * expose un StateFlow<UiState<T>> et charge ses données avec [load].
 */
abstract class BaseViewModel : ViewModel() {

    protected fun <T> stateHolder(): MutableStateFlow<UiState<T>> = MutableStateFlow(UiState.Loading)

    /**
     * Passe l'état à Loading, exécute [block], puis publie Content, Empty ou Error.
     * Toute exception est convertie en AppError par [ErrorMapper].
     */
    protected fun <T> load(
        state: MutableStateFlow<UiState<T>>,
        isEmpty: (T) -> Boolean = { false },
        block: suspend () -> T
    ) {
        viewModelScope.launch {
            state.value = UiState.Loading
            state.value = try {
                val data = block()
                if (isEmpty(data)) UiState.Empty else UiState.Content(data)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                UiState.Error(ErrorMapper.map(e))
            }
        }
    }
}