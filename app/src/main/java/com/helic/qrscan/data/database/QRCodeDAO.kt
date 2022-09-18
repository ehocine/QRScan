package com.helic.qrscan.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QRCodeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQRCode(qrCodeEntity: QRCodeEntity)

    @Query("SELECT * FROM qrcode_database_table ORDER BY id ASC")
    fun readQRCode(): Flow<List<QRCodeEntity>>

    @Delete
    suspend fun deleteQRCode(qrCodeEntity: QRCodeEntity)

    @Query("DELETE FROM qrcode_database_table")
    suspend fun deleteAllQRCode()
}