package com.helic.qrscan.di

import android.content.Context
import androidx.room.Room
import com.helic.qrscan.data.database.QRCodeDatabase
import com.helic.qrscan.utils.Constants.DATABASE_NAME
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        QRCodeDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: QRCodeDatabase) = database.qrCodeDao()

}