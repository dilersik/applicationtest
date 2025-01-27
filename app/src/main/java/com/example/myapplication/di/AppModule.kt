package com.example.myapplication.di

import com.example.myapplication.api.Api
import com.example.myapplication.repository.MyListRepository
import com.example.myapplication.repository.MyListRepositoryImpl
import com.example.myapplication.utils.EncryptionUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): Api = Retrofit.Builder()
        .baseUrl("https://mocki.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)

    @Provides
    @Singleton
    fun provideMyListRepository(api: Api): MyListRepository = MyListRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideEncryptionUtils(): EncryptionUtils = EncryptionUtils()
}