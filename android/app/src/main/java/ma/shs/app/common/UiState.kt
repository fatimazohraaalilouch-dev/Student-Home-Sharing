package ma.shs.app.common

/** Les 4 états de tout écran qui charge des données (docs/02-android-couches.mmd). */
sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Content<T>(val data: T) : UiState<T>
    object Empty : UiState<Nothing>
    data class Error(val error: AppError) : UiState<Nothing>
}