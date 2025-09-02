import Foundation
import SocketIO

@objc
public enum SocketIoTransport: Int {
    case websocket
    case polling
    case all
}

@objc
public class SocketIo: NSObject {
    private let socketManager: SocketManager
    private let socket: SocketIOClient

    @objc
    public init(
        uri: String,
        forceNew: Bool,
        transport: SocketIoTransport,
        path: String,
        query: [String: String]?,
        extraHeaders: [String: String]?,
        reconnection: Bool,
        reconnectionAttempts: Int,
        reconnectionDelay: Int,
        reconnectionDelayMax: Int,
        randomizationFactor: Double
    ) {
        var configuration: SocketIOClientConfiguration = []
        configuration.insert(.forceNew(forceNew))
        switch transport {
        case .websocket:
            configuration.insert(.forceWebsockets(true))
        case .polling:
            configuration.insert(.forcePolling(true))
        case .all: do {
        }
        }
        configuration.insert(.path(path))
        if let query = query {
            configuration.insert(.connectParams(query))
        }
        if let extraHeaders = extraHeaders {
            configuration.insert(.extraHeaders(extraHeaders))
        }
        configuration.insert(.reconnects(reconnection))
        configuration.insert(.reconnectAttempts(reconnectionAttempts))
        configuration.insert(.reconnectWait(reconnectionDelay))
        configuration.insert(.reconnectWaitMax(reconnectionDelayMax))
        configuration.insert(.randomizationFactor(randomizationFactor))

        socketManager = SocketManager(
            socketURL: URL(string: uri)!,
            config: configuration
        )
        socket = socketManager.defaultSocket
    }

    @objc
    public func connect() {
        socket.connect()
    }

    @objc
    public func disconnect() {
        socket.disconnect()
    }

    @objc
    public func isConnected() -> Bool {
        return socket.status == SocketIOStatus.connected
    }

    @objc
    public func on(event: String, action: @escaping ([Any]) -> Void) {
        socket.on(event) { data, emitter in
            action(data)
        }
    }

    @objc
    public func emit(event: String, data: String) {
        socket.emit(event, data, completion: nil)
    }

    @objc
    public func emit(event: String, data: String, acknowledge: @escaping ([Any]) -> Void) {
        socket.emitWithAck(event, data).timingOut(after: 5) { data in
            acknowledge(data)
        }
    }
}
