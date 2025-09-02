Pod::Spec.new do |spec|
    spec.name                     = 'KmpSocketIO'
    spec.version                  = '1.0.0'
    spec.homepage                 = 'https://github.com/terrakok/kmp-socketio'
    spec.source                   = { :git => 'https://github.com/terrakok/kmp-socketio' }
    spec.authors                  = 'terrakok'
    spec.license                  = { :type => 'MIT' }
    spec.summary                  = 'Objc SocketIO Wrapper'

    spec.module_name              = "ObjcSocketIoWrapper"
    spec.source_files             = 'kmp-socket-io/ObjcSocketIoWrapper/Sources/ObjcSocketIoWrapper/**.swift'

    spec.dependency 'Socket.IO-Client-Swift', '~> 16.1.1'

    spec.ios.deployment_target  = '16.0'
    spec.swift_version = '5.0'
end