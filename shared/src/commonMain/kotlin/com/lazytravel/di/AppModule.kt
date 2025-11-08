package com.lazytravel.di

import com.lazytravel.data.repository.DestinationRepositoryImpl
import com.lazytravel.domain.repository.DestinationRepository
import com.lazytravel.domain.usecase.GetDestinationsUseCase
import com.lazytravel.presentation.DestinationViewModel

/**
 * Dependency Injection - Khởi tạo các dependencies
 * Đơn giản, không sử dụng DI framework
 */
object AppModule {

    private val destinationRepository: DestinationRepository by lazy {
        DestinationRepositoryImpl()
    }

    private val getDestinationsUseCase: GetDestinationsUseCase by lazy {
        GetDestinationsUseCase(destinationRepository)
    }

    fun provideDestinationViewModel(): DestinationViewModel {
        return DestinationViewModel(getDestinationsUseCase)
    }
}
