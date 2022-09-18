package com.helic.qrscan.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.helic.qrscan.data.model.QRCode

class QRCodeTypeConverter {

    var gson = Gson()


    @TypeConverter
    fun resultToString(qrCode: QRCode): String {
        return gson.toJson(qrCode)
    }

    @TypeConverter
    fun stringToResult(data: String): QRCode {
        val listType = object : TypeToken<QRCode>() {}.type
        return gson.fromJson(data, listType)
    }

}