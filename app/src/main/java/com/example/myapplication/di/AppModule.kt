package com.example.myapplication.di

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.api.Api
import com.example.myapplication.repository.MyListRepository
import com.example.myapplication.repository.MyListRepositoryImpl
import com.example.myapplication.utils.EncryptionUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)

    @Provides
    @Singleton
    fun provideMyListRepository(api: Api): MyListRepository = MyListRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("myApp", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideEncryptionUtils(): EncryptionUtils = EncryptionUtils()
}