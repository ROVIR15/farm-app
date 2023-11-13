package com.vt.vt.core.data.di

import android.content.Context
import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.dummy.SessionFeedingDataStoreManager
import com.vt.vt.core.data.source.remote.dummy.auth.SessionPreferencesDataStoreManager
import com.vt.vt.core.data.source.remote.dummy.cobahilt.model.IAnimal
import com.vt.vt.core.data.source.remote.dummy.cobahilt.repository.AnimalRepositoryImpl
import com.vt.vt.core.data.source.remote.dummy.keuangan.IPengeluaran
import com.vt.vt.core.data.source.remote.dummy.keuangan.PengeluaranRepositoryImpl
import com.vt.vt.core.data.source.remote.dummy.list_animal_cage.AnimalCageRepositoryImpl
import com.vt.vt.core.data.source.remote.dummy.list_animal_cage.IAnimalCage
import com.vt.vt.core.data.source.remote.dummy.list_animal_matings.AnimalMatingsRepositoryImpl
import com.vt.vt.core.data.source.remote.dummy.list_animal_matings.IAnimalMatings
import com.vt.vt.core.data.source.remote.dummy.livestock.IPakan
import com.vt.vt.core.data.source.remote.dummy.livestock.PakanRepositoryImpl
import com.vt.vt.core.data.source.remote.dummy.tablayout.historyperanakan.HistoryPeranakanRepositoryImpl
import com.vt.vt.core.data.source.remote.dummy.tablayout.historyperanakan.IHistoryPeranakan
import com.vt.vt.core.data.source.repository.BlockAndAreasVtRepository
import com.vt.vt.core.data.source.repository.BreedingVtRepository
import com.vt.vt.core.data.source.repository.BudgetVtRepository
import com.vt.vt.core.data.source.repository.CategoriesVtRepository
import com.vt.vt.core.data.source.repository.DataRepository
import com.vt.vt.core.data.source.repository.ExpenditureVtRepository
import com.vt.vt.core.data.source.repository.FarmProfileVtRepository
import com.vt.vt.core.data.source.repository.FeedingVtRepository
import com.vt.vt.core.data.source.repository.HealthRecordVtRepository
import com.vt.vt.core.data.source.repository.HeightRecordVtRepository
import com.vt.vt.core.data.source.repository.IncomeVtRepository
import com.vt.vt.core.data.source.repository.LivestockVtRepository
import com.vt.vt.core.data.source.repository.ProductsVtRepository
import com.vt.vt.core.data.source.repository.SledsVtRepository
import com.vt.vt.core.data.source.repository.WeightRecordVtRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideInterfaceAnimal(): IAnimal = AnimalRepositoryImpl()

    @Provides
    @Singleton
    fun provideInterfaceLivestock(): IPakan = PakanRepositoryImpl()

    @Provides
    @Singleton
    fun provideInterfaceAnimalCage(): IAnimalCage = AnimalCageRepositoryImpl()

    @Provides
    @Singleton
    fun provideInterfaceAnimalMatings(): IAnimalMatings = AnimalMatingsRepositoryImpl()

    @Provides
    @Singleton
    fun provideInterfacePengeluaran(): IPengeluaran = PengeluaranRepositoryImpl()

    @Provides
    @Singleton
    fun provideInterfaceHistoryPeranakan(): IHistoryPeranakan = HistoryPeranakanRepositoryImpl()

    @Provides
    @Singleton
    fun provideDataRepository(apiService: ApiService): DataRepository = DataRepository(apiService)

    @Provides
    @Singleton
    fun provideVtRepository(apiService: ApiService): BlockAndAreasVtRepository =
        BlockAndAreasVtRepository(apiService)

    @Provides
    @Singleton
    fun provideSledsVtRepository(apiService: ApiService): SledsVtRepository =
        SledsVtRepository(apiService)

    @Provides
    @Singleton
    fun provideLivestockVtRepository(apiService: ApiService): LivestockVtRepository =
        LivestockVtRepository(apiService)

    @Provides
    @Singleton
    fun provideCategoriesVtRepository(apiService: ApiService): CategoriesVtRepository =
        CategoriesVtRepository(apiService)

    @Provides
    @Singleton
    fun provideProductsVtRepository(apiService: ApiService): ProductsVtRepository =
        ProductsVtRepository(apiService)

    @Provides
    @Singleton
    fun provideWeightRecordVtRepository(apiService: ApiService): WeightRecordVtRepository =
        WeightRecordVtRepository(apiService)

    @Provides
    @Singleton
    fun provideHeightRecordVtRepository(apiService: ApiService): HeightRecordVtRepository =
        HeightRecordVtRepository(apiService)

    @Provides
    @Singleton
    fun provideHealthRecordVtRepository(apiService: ApiService): HealthRecordVtRepository =
        HealthRecordVtRepository(apiService)

    @Provides
    @Singleton
    fun provideFarmProfileVtRepository(apiService: ApiService): FarmProfileVtRepository =
        FarmProfileVtRepository(apiService)

    @Provides
    @Singleton
    fun provideFeedingVtRepository(apiService: ApiService): FeedingVtRepository =
        FeedingVtRepository(apiService)

    @Provides
    @Singleton
    fun provideBreedingVtRepository(apiService: ApiService): BreedingVtRepository =
        BreedingVtRepository(apiService)

    @Provides
    @Singleton
    fun providesBudgetVtRepository(apiService: ApiService): BudgetVtRepository =
        BudgetVtRepository(apiService)

    @Provides
    @Singleton
    fun providesExpenditureVtRepository(apiService: ApiService): ExpenditureVtRepository =
        ExpenditureVtRepository(apiService)

    @Provides
    @Singleton
    fun providesIncomeVtRepository(apiService: ApiService): IncomeVtRepository =
        IncomeVtRepository(apiService)

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext appContext: Context): SessionPreferencesDataStoreManager =
        SessionPreferencesDataStoreManager(appContext)


    @Provides
    @Singleton
    fun provideFeedingDataStoreManager(@ApplicationContext appContext: Context): SessionFeedingDataStoreManager =
        SessionFeedingDataStoreManager(appContext)
}