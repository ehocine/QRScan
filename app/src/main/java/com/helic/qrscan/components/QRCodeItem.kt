package com.helic.qrscan.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.helic.qrscan.data.model.QRCode
import com.helic.qrscan.ui.theme.CardColor
import com.helic.qrscan.utils.QROptions

@Composable
fun QRCodeItem(qrCode: QRCode, onItemClicked: (qrCode: QRCode) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = {
                onItemClicked(qrCode)
            }),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.CardColor
    ) {
        when (qrCode.type) {
            QROptions.Text -> {
                TextItem(qrCode, true)
            }
            QROptions.URL -> {
                URLItem(qrCode, true)
            }
            QROptions.Email -> {
                EmailItem(qrCode, true)
            }
            QROptions.PhoneNumber -> {
                PhoneNumberItem(qrCode, true)
            }
            QROptions.SMS -> {
                SMSItem(qrCode, true)
            }
            QROptions.WiFi -> {
                WifiItem(qrCode, true)
            }
            QROptions.GeographicLocation -> {
                GeoGraphicItem(qrCode, true)
            }
            else -> {
                TextItem(qrCode, true)
            }
        }
    }
}