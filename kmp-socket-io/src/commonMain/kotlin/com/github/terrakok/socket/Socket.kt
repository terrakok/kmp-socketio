package com.github.terrakok.socket

expect class Socket(
    uri: String,
    options: SocketOptions = SocketOptions(),
    build: SocketBuilder.() -> Unit
) {
    fun connect()
    fun disconnect()
    fun isConnected(): Boolean
    fun emit(event: String, data: String)
    fun emit(event: String, data: String, acknowledge: Socket.(messages: List<String>) -> Unit)
}
