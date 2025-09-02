package com.github.terrakok.socket

enum class SocketEvent(
    internal val eventNames: Array<String>
) {
    Connecting(arrayOf("connecting")),
    Connect(arrayOf("connect")),
    Disconnect(arrayOf("disconnect")),
    Error(arrayOf("error", "connect_error", "reconnect_error")),
    Reconnect(arrayOf("reconnect")),
    ReconnectAttempt(arrayOf("reconnect_attempt")),
    Ping(arrayOf("ping")),
    Pong(arrayOf("pong"));
}