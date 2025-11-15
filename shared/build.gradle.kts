import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "2.2.21"
    id("org.jetbrains.compose") version "1.9.3"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21"
}

kotlin {
    // Apply Default Hierarchy Template explicitly for Kotlin 2.0+
    // This automatically creates iosMain and sets up all dependencies
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate()

    androidTarget {
        @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        }
    }

    // Create XCFramework for iOS
    val xcf = XCFramework("shared")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true

            // Export Compose dependencies to iOS framework
            export(compose.runtime)
            export(compose.foundation)
            export(compose.material3)
            export(compose.ui)

            xcf.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

                // Ktor for HTTP requests
                implementation("io.ktor:ktor-client-core:3.3.2")
                implementation("io.ktor:ktor-client-content-negotiation:3.3.2")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.2")
                implementation("io.ktor:ktor-client-logging:3.3.2")

                // Koin for Dependency Injection
                implementation("io.insert-koin:koin-core:4.1.1")

                implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.21")

                // Compose Multiplatform - use api() for iOS framework export
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
            }
        }
        val androidMain by getting {
            resources.srcDir("src/androidMain/resources")
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
                implementation("io.ktor:ktor-client-android:3.3.2")

                // Koin for Android
                implementation("io.insert-koin:koin-android:4.1.1")
            }
        }

        // iOS source sets - automatically created by Default Hierarchy Template
        // All dependencies (iosMain -> commonMain, iosX64Main -> iosMain, etc.) are automatic
        val iosMain by getting {
            resources.srcDir("src/iosMain/resources")
            dependencies {
                implementation("io.ktor:ktor-client-darwin:3.3.2")
            }
        }
    }
}

android {
    namespace = "com.lazytravel.shared"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
