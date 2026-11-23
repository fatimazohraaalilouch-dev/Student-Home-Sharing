package ma.shs.app.ui.demo

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ma.shs.app.common.BaseViewModel
import ma.shs.app.common.UiState
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(
    private val repository: DemoRepository
) : BaseViewModel() {

    private val _state = stateHolder<List<String>>()
    val state: StateFlow<UiState<List<String>>> = _state

    init {
        refresh(DemoMode.CONTENT)
    }

    fun refresh(mode: DemoMode) {
        load(_state, isEmpty = { it.isEmpty() }) { repository.items(mode) }
    }
}