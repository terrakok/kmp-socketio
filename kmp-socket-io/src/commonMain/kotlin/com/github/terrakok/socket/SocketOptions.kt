package com.github.terrakok.socket

data class SocketOptions(
    val forceNew: Boolean = false,
    val transport: Transport = Transport.All,
    val path: String = "/socket.io/",
    val query: Map<String, String>? = null,
    val extraHeaders: Map<String, List<String>>? = null,
    val reconnection: Boolean = true,
    val reconnectionAttempts: Int = -1,
    val reconnectionDelay: Long = 1_000,
    val reconnectionDelayMax: Long = 5_000,
    val randomizationFactor: Double = 0.5,
) {
    enum class Transport { WebSocket, Polling, All }
}