package de.malteans.pixlists.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.math.absoluteValue

fun Float.toHex(): String {
    val hex = (this * 255).toInt().absoluteValue.coerceAtMost(255).toString(16)
    return if (hex.length == 1) "0$hex" else hex
}

fun Long.toStringDate(withYear:Boolean = false): String {
    Instant.fromEpochMilliseconds(this).let { instant ->
        instant.toLocalDateTime(TimeZone.UTC).let { localDateTime ->
            return "${localDateTime.dayOfMonth}.${localDateTime.monthNumber}" +
                    if (withYear) ".${localDateTime.year}" else ""
        }
    }
}

fun String.toLongDate(): Long? {
    try {
        val splits = this.split(".")
        val day = splits[0].toInt()
        val month = splits[1].toInt()
        val year = if (splits.size == 2 || splits[2] == "") Clock.System.now().toLocalDateTime(TimeZone.UTC).year
            else splits[2].toInt()
        return LocalDate(year, month, day).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    } catch (e: Exception) {
        return null
    }
}

fun String.isValidDate(): Boolean {
    return this.toLongDate() != null
}