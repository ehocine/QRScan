package com.helic.qrscan.data.model

import com.helic.qrscan.utils.QROptions


data class QRCode(
    val status: Status = Status.Scanned,
    val type: QROptions = QROptions.Text,
    val content: String = ""
)

enum class Status {
    Scanned,
    Generated
}