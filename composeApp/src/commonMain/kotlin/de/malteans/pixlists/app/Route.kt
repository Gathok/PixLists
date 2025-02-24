package de.malteans.pixlists.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object List: Route

    @Serializable
    data class ViewList(val listId: Int): Route
}