package de.malteans.pixlists.app

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data class ListScreen(
        val curPixListId: Long? = null,
    )

    @Serializable
    object ManageColorsScreen

    @Serializable
    object LoadingScreen
}