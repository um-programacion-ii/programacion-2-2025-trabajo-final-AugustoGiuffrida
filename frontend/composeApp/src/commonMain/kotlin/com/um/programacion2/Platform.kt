package com.um.programacion2

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform