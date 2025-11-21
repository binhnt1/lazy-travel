package com.lazytravel.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Koin Setup - Dependency Injection Initialization
 *
 * Khởi tạo Koin với tất cả modules:
 * - dataModule: Repository implementations
 * - domainModule: Use cases
 * - presentationModule: ViewModels
 *
 * Cách sử dụng:
 *
 * Android (MainActivity.onCreate):
 * ```
 * initKoin()
 * ```
 *
 * iOS (AppDelegate.application didFinishLaunchingWithOptions):
 * ```swift
 * KoinSetupKt.doInitKoin()
 * ```
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}): KoinApplication {
    return startKoin {
        appDeclaration()
    }
}

/**
 * iOS Helper - Initialize Koin without parameters
 */
fun initKoin() = initKoin {}
