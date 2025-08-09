package de.malteans.pixlists.app

import org.jetbrains.compose.resources.StringResource
import pixlists.composeapp.generated.resources.Res
import pixlists.composeapp.generated.resources.de
import pixlists.composeapp.generated.resources.en

enum class AppLang(
    val code: String,
    val StringRes: StringResource,
) {
    English("en", Res.string.en),
    German("de", Res.string.de),
}