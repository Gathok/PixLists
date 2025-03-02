package de.malteans.pixlists.presentation.manageColors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.malteans.pixlists.domain.PixColor
import de.malteans.pixlists.domain.PixRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ManageColorsViewModel(
    private val repository: PixRepository
): ViewModel() {

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

    private val _state = MutableStateFlow(ManageColorsState())

    val state = combine(
        _state, _allPixLists.onStart { emit(emptyList()) }, _colorList.onStart { emit(emptyList()) }
    ) { state, allPixLists, colorList ->
        state.copy(
            colorList = colorList,
            allCategories = allPixLists.flatMap { it.categories },
            colorUses = _colorList.value.associate { color ->
                color.id to
                _allPixLists.value.sumOf { pixList ->
                    pixList.categories.count { it.color?.id == color.id }
                }
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ManageColorsState())

    fun loadDefaultColors() {
        val invalideNames = _colorList.value.map { it.name }
        listOf(
            PixColor(name = "Peach", red = 1.0f, green = 0.87f, blue = 0.77f),
            PixColor(name = "Lemon Yellow", red = 1.0f, green = 0.97f, blue = 0.69f),
            PixColor(name = "Mint Green", red = 0.74f, green = 0.98f, blue = 0.79f),
            PixColor(name = "Sky Blue", red = 0.68f, green = 0.85f, blue = 0.90f),
            PixColor(name = "Lavender", red = 0.82f, green = 0.75f, blue = 0.93f),
            PixColor(name = "Dusty Pink", red = 0.91f, green = 0.75f, blue = 0.80f),
            PixColor(name = "Pale Orange", red = 1.0f, green = 0.85f, blue = 0.72f),
            PixColor(name = "Baby Blue", red = 0.68f, green = 0.90f, blue = 1.0f),
            PixColor(name = "Blush Pink", red = 1.0f, green = 0.82f, blue = 0.86f),
            PixColor(name = "Pastel Lilac", red = 0.91f, green = 0.78f, blue = 0.94f)
        ).filter { it.name !in invalideNames }.forEach { color ->
            addColor(color.name, color.red, color.green, color.blue)
        }
    }

    fun deleteUnusedColors() {
        viewModelScope.launch {
            val unusedColors = _colorList.value.filter { color ->
                _allPixLists.value.none { pixList ->
                    pixList.categories.any { it.color?.id == color.id }
                }
            }
            unusedColors.forEach { color ->
                deleteColor(color)
            }
        }
    }

    // Color DB operations
    fun addColor(name: String, red: Float, green: Float, blue: Float) {
        viewModelScope.launch {
            repository.createColor(name, red, green, blue)
        }
    }

    fun updateColor(colorToEdit: PixColor, newName: String?, newRgb: List<Float>?) {
        viewModelScope.launch {
            if (newName != null) {
                repository.renameColor(colorToEdit.id, newName)
            }
            if (newRgb != null) {
                repository.changeColor(colorToEdit.id, newRgb[0], newRgb[1], newRgb[2])
            }
        }
    }

    fun deleteColor(color: PixColor) {
        viewModelScope.launch {
            repository.deleteColor(color.id)
        }
    }
}