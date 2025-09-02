package com.github.terrakok.socket

interface SocketBuilder {
    fun on(event: String, action: Socket.(messages: List<String>) -> Unit)
    fun on(vararg events: String, action: Socket.(messages: List<String>) -> Unit) {
        events.forEach { on(it, action) }
    }
    fun on(event: SocketEvent, action: Socket.(messages: List<String>) -> Unit) {
        on(*event.eventNames, action = action)
    }
    fun on(vararg socketEvents: SocketEvent, action: Socket.(messages: List<String>) -> Unit) {
        socketEvents.forEach { on(it, action) }
    }
}