package de.malteans.pixlists.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class PixDate(
    val month: Int,
    val day: Int
) {
    constructor(month: Months, day: Int) : this(month.ordinal + 1, day)

    override fun toString(): String {
        var result = ""
        if (month < 10) result += "0$month" else result += month
        result += "-"
        if (day < 10) result += "0$day" else result += day
        return result
    }

    fun toEpochMilliseconds(year: Int = Clock.System.now().toLocalDateTime(TimeZone.UTC).year): Long { // TODO: Implement year usage
        return LocalDateTime(year, month, day, 0, 0).toInstant(TimeZone.UTC).toEpochMilliseconds()
    }

    companion object {
        fun fromString(date: String): PixDate {
            val parts = date.split("-")
            return PixDate(parts[0].toInt(), parts[1].toInt())
        }
    }
}