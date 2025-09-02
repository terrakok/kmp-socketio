// swift-tools-version: 6.0
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "ObjcSocketIoWrapper",
    products: [
        // Products define the executables and libraries a package produces, making them visible to other packages.
        .library(
            name: "ObjcSocketIoWrapper",
            targets: ["ObjcSocketIoWrapper"]),
    ],
    dependencies: [
        .package(url: "https://github.com/socketio/socket.io-client-swift", exact: "16.1.1")
    ],
    targets: [
        // Targets are the basic building blocks of a package, defining a module or a test suite.
        // Targets can depend on other targets in this package and products from dependencies.
        .target(
            name: "ObjcSocketIoWrapper",
            dependencies: [
                .product(name: "SocketIO", package: "socket.io-client-swift")
            ]
        ),
    ]
)
