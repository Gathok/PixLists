package de.malteans.pixlists

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform