package de.malteans.pixlists.list.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteans.pixlists.list.domain.PixRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: PixRepository
): ViewModel() {

    private val _state = MutableStateFlow(MainState())

    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.CreateList -> {
                viewModelScope.launch {
                    repository.createList(action.listName)
                }
            }
            is MainAction.SelectList -> TODO()
        }
    }
}