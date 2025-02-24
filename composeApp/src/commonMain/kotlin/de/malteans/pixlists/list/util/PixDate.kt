package de.malteans.pixlists.list.util

data class PixDate(
    val moth: Int,
    val day: Int
) {
    override fun toString(): String {
        var result = ""
        if (moth < 10) result += "0$moth" else result += moth
        result += "-"
        if (day < 10) result += "0$day" else result += day
        return result
    }

    companion object {
        fun fromString(date: String): PixDate {
            val parts = date.split("-")
            return PixDate(parts[0].toInt(), parts[1].toInt())
        }
    }
}