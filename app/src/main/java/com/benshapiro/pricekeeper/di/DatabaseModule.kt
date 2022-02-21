package com.benshapiro.pricekeeper.di

import android.content.Context
import androidx.room.Room
import com.benshapiro.pricekeeper.data.local.PriceDao
import com.benshapiro.pricekeeper.data.local.PriceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : PriceDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PriceDatabase::class.java,
            "table_prices",
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePriceDao(priceDatabase: PriceDatabase) : PriceDao {
        return priceDatabase.getPriceDao()
    }

}