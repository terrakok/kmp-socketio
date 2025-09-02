package com.github.terrakok.socket

import cocoapods.ObjcSocketIoWrapper.SocketIo
import cocoapods.ObjcSocketIoWrapper.SocketIoTransportAll
import cocoapods.ObjcSocketIoWrapper.SocketIoTransportPolling
import cocoapods.ObjcSocketIoWrapper.SocketIoTransportWebsocket
import com.github.terrakok.socket.SocketOptions.Transport
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual class Socket actual constructor(
    uri: String,
    options: SocketOptions,
    build: SocketBuilder.() -> Unit
) {
    val socketIO: SocketIo

    init {
        socketIO = SocketIo(
            uri = uri,
            forceNew = options.forceNew,
            transport = when (options.transport) {
                Transport.WebSocket -> SocketIoTransportWebsocket
                Transport.Polling -> SocketIoTransportPolling
                Transport.All -> SocketIoTransportAll
            },
            path = options.path,
            query = options.query as? Map<Any?, *>,
            extraHeaders = options.extraHeaders as? Map<Any?, *>,
            reconnection = options.reconnection,
            reconnectionAttempts = options.reconnectionAttempts.toLong(),
            reconnectionDelay = options.reconnectionDelay,
            reconnectionDelayMax = options.reconnectionDelayMax,
            randomizationFactor = options.randomizationFactor,
        )

        val socketBuilder = object : SocketBuilder {
            override fun on(
                event: String,
                action: Socket.(messages: List<String>) -> Unit
            ) {
                socketIO.onEvent(event) { args ->
                    action(args.asKotlinSocketData())
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

    actual fun isConnected(): Boolean = socketIO.isConnected()

    actual fun emit(event: String, data: String) {
        socketIO.emitWithEvent(event, data)
    }

    actual fun emit(
        event: String,
        data: String,
        acknowledge: Socket.(messages: List<String>) -> Unit
    ) {
        socketIO.emitWithEvent(event, data) { args ->
            acknowledge(args.asKotlinSocketData())
        }
    }

    private fun List<*>?.asKotlinSocketData(): List<String> =
        this.orEmpty().map { it.toString() }
}