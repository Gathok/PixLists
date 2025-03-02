package de.malteans.pixlists.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteans.pixlists.domain.PixCategory
import de.malteans.pixlists.domain.PixColor
import de.malteans.pixlists.domain.PixRepository
import de.malteans.pixlists.util.Months
import de.malteans.pixlists.util.PixDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository: PixRepository,
): ViewModel() {

    private val _curPixListId = MutableStateFlow<Long?>(null)

    private val _allPixLists = repository
        .getAllPixLists()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _colorList = repository
        .getAllColors()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _state = MutableStateFlow(ListState())

    val state = combine(
        _state, _curPixListId, _allPixLists.onStart { emit(emptyList()) }, _colorList.onStart { emit(emptyList()) }
    ) { state, curPixListId, allPixLists, colorList ->
        val curPixList = allPixLists.find { it.id == curPixListId }
        state.copy(
            curPixList = curPixList,
            curCategories = curPixList?.categories ?: emptyList(),
            colorList = colorList,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ListState()
    )

    fun setPixListId(pixListId: Long?) {
        _curPixListId.value = pixListId
    }

    fun getInvalideNames(): List<String> {
        return _allPixLists.value.map { it.name }
    }

    fun updatePixListName(newName: String) {
        viewModelScope.launch {
            repository.renameList(_curPixListId.value!!, newName)
        }
    }

    // PixCategory functions -------------------------------------------------
    fun createPixCategory(name: String, color: PixColor) {
        viewModelScope.launch {
            repository.createCategory(_curPixListId.value!!, color.id, name)
        }
    }

    fun updatePixCategory(category: PixCategory, newName: String?, newColor: PixColor?) {
        viewModelScope.launch {
            if (newName != null) {
                repository.renameCategory(category.id, newName)
            }
            if (newColor != null) {
                repository.changeCategoryColor(category.id, newColor.id)
            }
        }
    }

    fun deletePixCategory(category: PixCategory, undo: Boolean = false) {
        viewModelScope.launch {
            repository.deleteCategory(category.id)
        }
    }

    // PixEntry functions ---------------------------------------------------
    fun setPixEntry(day: Int, month: Months, category: PixCategory?) {
        viewModelScope.launch {
            if (category != null) {
                repository.createEntry(_curPixListId.value!!, category.id, PixDate(month, day))
            } else {
                repository.deleteEntry(_curPixListId.value!!, PixDate(month, day))
            }
        }
    }

    // Update Category Order ------------------------------------------------
    fun updateCategoryOrder(newOrder: List<PixCategory>) {
        viewModelScope.launch {
            TODO()
        }
    }
}