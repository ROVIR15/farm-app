package com.vt.vt.core.data.di

import android.content.Context
import com.vt.vt.core.data.source.remote.dummy.auth.SessionPreferencesDataStoreManager
import com.vt.vt.core.data.source.remote.dummy.cobahilt.model.IAnimal
import com.vt.vt.core.data.source.remote.dummy.cobahilt.repository.AnimalRepositoryImpl
import com.vt.vt.core.data.source.remote.dummy.list_animal_cage.AnimalCageRepositoryImpl
import com.vt.vt.core.data.source.remote.dummy.list_animal_cage.IAnimalCage
import com.vt.vt.core.data.source.remote.dummy.list_animal_matings.AnimalMatingsRepositoryImpl
import com.vt.vt.core.data.source.remote.dummy.list_animal_matings.IAnimalMatings
import com.vt.vt.core.data.source.remote.dummy.livestock.ILivestock
import com.vt.vt.core.data.source.remote.dummy.livestock.LivestockRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideInterfaceAnimal(): IAnimal = AnimalRepositoryImpl()

    @Provides
    @Singleton
    fun provideInterfaceLivestock(): ILivestock = LivestockRepositoryImpl()

    @Provides
    @Singleton
    fun provideInterfaceAnimalCage(): IAnimalCage = AnimalCageRepositoryImpl()

    @Provides
    @Singleton
    fun provideInterfaceAnimalMatings(): IAnimalMatings = AnimalMatingsRepositoryImpl()

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext appContext: Context): SessionPreferencesDataStoreManager =
        SessionPreferencesDataStoreManager(appContext)
}