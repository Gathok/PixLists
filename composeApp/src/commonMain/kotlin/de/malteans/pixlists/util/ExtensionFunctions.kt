package de.malteans.pixlists.util

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

fun Long.toStringDate(): String {
    Instant.fromEpochMilliseconds(this).let { instant ->
        instant.toLocalDateTime(TimeZone.UTC).let { localDateTime ->
            return "${localDateTime.dayOfMonth}.${localDateTime.monthNumber}.${localDateTime.year}"
        }
    }
}

fun Long.toPixDate(): PixDate {
    Instant.fromEpochMilliseconds(this).let { instant ->
        instant.toLocalDateTime(TimeZone.UTC).let { localDateTime ->
            return PixDate(localDateTime.monthNumber, localDateTime.dayOfMonth)
        }
    }
}

fun String.toLongDate(): Long? {
    try {
        val (day, month, year) = this.split(".").map { it.toInt() }
        return LocalDate(year, month, day).atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    } catch (e: Exception) {
        return null
    }
}