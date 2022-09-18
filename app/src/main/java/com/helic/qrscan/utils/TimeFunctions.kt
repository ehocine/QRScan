package com.helic.qrscan.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun convertTimeStampToDate(epoch: Long): String {
    val date = Date(epoch)
    val sdf = SimpleDateFormat("yyyyMMddHHmm")
    return sdf.format(date)
}