package de.malteans.pixlists.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteans.pixlists.domain.PixRepository
import de.malteans.pixlists.util.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: PixRepository,
): ViewModel() {

    private val _allPixLists = repository
        .getAllPixLists()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _state = MutableStateFlow(MainState())
    val state = combine(_state, _allPixLists) { state, allPixLists ->
        state.copy(
            allPixLists = allPixLists,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainState()
    )

    fun createPixList(name: String): Long {
        var id = 0L
        viewModelScope.launch {
            id = repository.createList(name)
        }
        return id // FIXME: DAS FUNKTIONIERT WAHRSCHEINLICH NICHT
    }

    fun deletePixListById(id: Long) {
        viewModelScope.launch {
            repository.deleteListById(id)
        }
    }

    fun setCurPixListId(id: Long?) {
        _state.value = _state.value.copy(curPixListId = id)
    }

    fun setCurScreen(screen: Screen) {
        _state.value = _state.value.copy(curScreen = screen)
    }
}