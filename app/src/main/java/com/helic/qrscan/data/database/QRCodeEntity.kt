package com.helic.qrscan.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.helic.qrscan.data.model.QRCode
import com.helic.qrscan.utils.Constants.QRCODE_TABLE

@Entity(tableName = QRCODE_TABLE)
class QRCodeEntity (

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var qrCode: QRCode = QRCode()

)