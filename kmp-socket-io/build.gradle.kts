@file:OptIn(
    org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class,
    org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class
)

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    androidTarget { publishLibraryVariants("release") }
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate {
        common {
            group("jvmCommon") {
                withAndroidTarget()
                withJvm()
            }
        }
    }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        val jvmCommonMain by getting
        jvmCommonMain.dependencies {
            implementation("io.socket:socket.io-client:2.1.2") {
                exclude(group = "org.json", module = "json")
            }
        }
        jvmMain.dependencies {
            implementation("org.json:json:20250517")
        }
    }

    cocoapods {
        version = "1.0.0"
        ios.deploymentTarget = "16.1"
        noPodspec()
        pod(name = "ObjcSocketIoWrapper") {
            source = path(project.file("ObjcSocketIoWrapper"))
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }
}

android {
    namespace = "com.github.terrakok.socket"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
    }
}

//Publishing your Kotlin Multiplatform library to Maven Central
//https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html
mavenPublishing {
    publishToMavenCentral()
    coordinates("com.github.terrakok", "kmp-socketio", "1.0.0")

    pom {
        name = "KmpSocketIO"
        description = "Kotlin Multiplatform library for Socket.IO"
        url = "https://github.com/terrakok/kmp-socketio"

        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
            }
        }

        developers {
            developer {
                id = "terrakok"
                name = "Konstantin Tskhovrebov"
                email = "terrakok@gmail.com"
            }
        }

        scm {
            url = "https://github.com/terrakok/kmp-socketio"
        }
    }
    if (project.hasProperty("signing.keyId")) signAllPublications()
}
