package com.helic.qrscan.data.repository

import com.helic.qrscan.data.database.QRCodeDAO
import com.helic.qrscan.data.database.QRCodeEntity
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(private val qrCodeDAO: QRCodeDAO) {

    fun readQRCode(): Flow<List<QRCodeEntity>> {
        return qrCodeDAO.readQRCode()
    }


    suspend fun insertQRCode(qrCodeEntity: QRCodeEntity) {
        qrCodeDAO.insertQRCode(qrCodeEntity)
    }

    suspend fun deleteQRCode(qrCodeEntity: QRCodeEntity) {
        qrCodeDAO.deleteQRCode(qrCodeEntity)
    }

    suspend fun deleteAllQRCode() {
        qrCodeDAO.deleteAllQRCode()
    }
}