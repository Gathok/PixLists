package de.malteans.pixlists.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteans.pixlists.domain.PixRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: PixRepository,
): ViewModel() {

    private val _curPixListId: MutableStateFlow<Long?> = MutableStateFlow(null)

    private val _allPixLists = repository
        .getAllPixLists()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _state = MutableStateFlow(MainState())
    val state = combine(_state, _curPixListId, _allPixLists) { state, curPixListId, allPixLists ->
        state.copy(
            allPixLists = allPixLists,
            curPixList = allPixLists.find { it.id == curPixListId },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainState()
    )

    // PixList functions -----------------------------------------------------
    fun createPixList(name: String): Long {
        var id = 0L
        viewModelScope.launch {
            id = repository.createList(name)
        }
        return id // FIXME: DAS FUNKTIONIERT WAHRSCHEINLICH NICHT
    }

    fun setCurPixListById(id: Long) {
        _curPixListId.value = id
    }

    fun deletePixListById(id: Long) {
        viewModelScope.launch {
            repository.deleteListById(id)
        }
    }
}