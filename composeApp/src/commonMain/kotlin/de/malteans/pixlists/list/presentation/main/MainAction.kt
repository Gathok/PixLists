package de.malteans.pixlists.list.presentation.main

sealed interface MainAction {
    data class CreateList(val listName: String): MainAction
    data class SelectList(val listId: Int): MainAction
}