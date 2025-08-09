package de.malteans.pixlists.presentation.list.components

import de.malteans.pixlists.presentation.components.UiText
import kotlinx.datetime.DayOfWeek
import pixlists.composeapp.generated.resources.Res
import pixlists.composeapp.generated.resources.friday
import pixlists.composeapp.generated.resources.monday
import pixlists.composeapp.generated.resources.saturday
import pixlists.composeapp.generated.resources.sunday
import pixlists.composeapp.generated.resources.thursday
import pixlists.composeapp.generated.resources.tuesday
import pixlists.composeapp.generated.resources.wednesday

fun DayOfWeek.toUiText(): UiText {
    return UiText.FromStringResource(when (this) {
        DayOfWeek.MONDAY -> Res.string.monday
        DayOfWeek.TUESDAY -> Res.string.tuesday
        DayOfWeek.WEDNESDAY -> Res.string.wednesday
        DayOfWeek.THURSDAY -> Res.string.thursday
        DayOfWeek.FRIDAY -> Res.string.friday
        DayOfWeek.SATURDAY -> Res.string.saturday
        DayOfWeek.SUNDAY -> Res.string.sunday
    })
}