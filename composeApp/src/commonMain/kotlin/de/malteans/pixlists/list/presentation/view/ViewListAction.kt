package de.malteans.pixlists.list.presentation.view

sealed interface ViewListAction {
    data class LoadList(val listId: Int): ViewListAction
}