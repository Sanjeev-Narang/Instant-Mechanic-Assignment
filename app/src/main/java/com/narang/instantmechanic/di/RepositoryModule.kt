package com.narang.instantmechanic.di

import com.narang.instantmechanic.data.MechanicApi
import com.narang.instantmechanic.data.FakeMechanicRepository
import com.narang.instantmechanic.domain.MechanicRepository
import com.narang.instantmechanic.data.MechanicRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

/**
 * Minimal repo graph. Flip USE_FAKE to false once the Cloud Function is live.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    private const val USE_FAKE = true

    @Provides
    @Singleton
    fun provideMechanicRepository(api: MechanicApi): MechanicRepository {
        return if (USE_FAKE) {
            Timber.w("DI: Using FakeMechanicRepository (USE_FAKE=true)")
            FakeMechanicRepository()
        } else {
            MechanicRepositoryImpl(api)
        }
    }
}
