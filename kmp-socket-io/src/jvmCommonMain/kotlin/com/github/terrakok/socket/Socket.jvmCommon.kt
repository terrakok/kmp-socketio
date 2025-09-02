package com.github.terrakok.socket

import com.github.terrakok.socket.SocketOptions.Transport.All
import com.github.terrakok.socket.SocketOptions.Transport.Polling
import com.github.terrakok.socket.SocketOptions.Transport.WebSocket
import io.socket.client.IO
import io.socket.client.Socket as SocketIO
import io.socket.engineio.client.transports.Polling as POLLING
import io.socket.engineio.client.transports.WebSocket as WEB_SOCKET

actual class Socket actual constructor(
    uri: String,
    options: SocketOptions,
    build: SocketBuilder.() -> Unit
) {
    private val socketIO: SocketIO

    init {
        val optionsBuilder = IO.Options.builder()
        val transports = when (options.transport) {
            WebSocket -> arrayOf(WEB_SOCKET.NAME)
            Polling -> arrayOf(POLLING.NAME)
            All -> arrayOf(WEB_SOCKET.NAME, POLLING.NAME)
        }
        optionsBuilder.setTransports(transports)
        optionsBuilder.setPath(options.path)
        optionsBuilder.setForceNew(options.forceNew)
        optionsBuilder.setReconnection(options.reconnection)
        optionsBuilder.setReconnectionAttempts(
            if (options.reconnectionAttempts > -1) options.reconnectionAttempts else Int.MAX_VALUE
        )
        optionsBuilder.setReconnectionDelay(options.reconnectionDelay)
        optionsBuilder.setReconnectionDelayMax(options.reconnectionDelayMax)
        optionsBuilder.setRandomizationFactor(options.randomizationFactor)

        options.query?.entries
            ?.takeIf { it.isNotEmpty() }
            ?.joinToString("&") { (key, value) -> "$key=$value" }
            ?.let { optionsBuilder.setQuery(it) }

        options.extraHeaders
            ?.takeIf { it.isNotEmpty() }
            ?.let { optionsBuilder.setExtraHeaders(it) }

        socketIO = IO.socket(uri, optionsBuilder.build())

        val socketBuilder = object : SocketBuilder {
            override fun on(
                event: String,
                action: Socket.(messages: List<String>) -> Unit
            ) {
                socketIO.on(event) { args ->
                    action(args.map { it.toString() })
                }
            }
        }
        socketBuilder.build()
    }

    actual fun connect() {
        socketIO.connect()
    }

    actual fun disconnect() {
        socketIO.disconnect()
    }

    actual fun isConnected(): Boolean = socketIO.connected()

    actual fun emit(event: String, data: String) {
        socketIO.emit(event, data)
    }

    actual fun emit(
        event: String,
        data: String,
        acknowledge: Socket.(messages: List<String>) -> Unit
    ) {
        socketIO.emit(event, data, fun(args: Array<Any>) {
            acknowledge(args.map { it.toString() })
        })
    }
}