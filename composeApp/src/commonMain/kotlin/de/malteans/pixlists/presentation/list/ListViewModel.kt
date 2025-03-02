package de.malteans.pixlists.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteans.pixlists.domain.PixCategory
import de.malteans.pixlists.domain.PixColor
import de.malteans.pixlists.domain.PixList
import de.malteans.pixlists.domain.PixRepository
import de.malteans.pixlists.util.Months
import de.malteans.pixlists.util.PixDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ListViewModel(
    private val repository: PixRepository,
): ViewModel() {

    private val _curPixListId = MutableStateFlow<Long?>(null)

    private val _curPixList: Flow<PixList?> = _curPixListId
        .flatMapLatest { id ->
            if (id != null) repository.getCurrentPixList(id)
            else flowOf(null)
        }

    private val _state = MutableStateFlow(ListState())

    val state = combine(
        _state,
        _curPixList,
    ) { state, curPixList ->
        ListState(
            curPixList = curPixList,
            curCategories = curPixList?.categories ?: emptyList(),
            colorList = state.colorList,
            invalideNames = state.invalideNames.filter { it != curPixList?.name },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ListState()
    )

    fun setPixListId(pixListId: Long?) {
        _curPixListId.value = pixListId

        viewModelScope.launch {
            _state.value = _state.value.copy(
                invalideNames = repository.getAllPixLists().first().map { it.name },
                colorList = repository.getAllColors().first()
            )
        }
    }

    fun updatePixListName(newName: String) {
        viewModelScope.launch {
            repository.renameList(_curPixListId.value!!, newName)
            _state.value = _state.value.copy(
                invalideNames = repository.getAllPixLists().first().map { it.name },
            )
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

    fun deletePixCategory(category: PixCategory) {
        viewModelScope.launch {
            repository.deleteCategory(category.id)
        }
    }

    // PixEntry functions ---------------------------------------------------
    fun setPixEntry(day: Int, month: Months, category: PixCategory?) {
        viewModelScope.launch {
            if (category != null) {
                if (_curPixList.first()!!.entries.contains(PixDate(month, day))) {
                    repository.deleteEntry(_curPixListId.value!!, PixDate(month, day))
                }
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