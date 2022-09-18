package com.helic.qrscan.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [QRCodeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(QRCodeTypeConverter::class)
abstract class QRCodeDatabase : RoomDatabase() {
    abstract fun qrCodeDao(): QRCodeDAO
}