package com.helic.qrscan.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.helic.qrscan.R
import com.helic.qrscan.components.*
import com.helic.qrscan.data.database.QRCodeEntity
import com.helic.qrscan.data.model.QRCode
import com.helic.qrscan.data.model.Status
import com.helic.qrscan.ui.theme.ButtonColor
import com.helic.qrscan.ui.theme.TextColor
import com.helic.qrscan.utils.QROptions
import com.helic.qrscan.viewmodel.MainViewModel

@Composable
fun ResultScreen(
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
    ) {
        Title(title = "Scanning Result")
        Spacer(modifier = Modifier.padding(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = mainViewModel.result.value,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(9f)
            )
            IconButton(onClick = {
                val clipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("label", mainViewModel.result.value)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    tint = MaterialTheme.colors.TextColor,
                    contentDescription = ""
                )
            }
        }
        val type = if (mainViewModel.result.value.startsWith("WIFI", ignoreCase = true)) {
            QROptions.WiFi
        } else if (mainViewModel.result.value.startsWith("geo", ignoreCase = true)) {
            QROptions.GeographicLocation
        } else if (mainViewModel.result.value.startsWith("sms", ignoreCase = true)) {
            QROptions.SMS
        } else if (mainViewModel.result.value.startsWith("tel", ignoreCase = true)) {
            QROptions.PhoneNumber
        } else if (mainViewModel.result.value.startsWith("mailto", ignoreCase = true)) {
            QROptions.Email
        } else if (mainViewModel.result.value.startsWith(
                "URLTO",
                ignoreCase = true
            ) || mainViewModel.result.value.startsWith("HTTP", ignoreCase = true)
        ) {
            QROptions.URL
        } else {
            QROptions.Text
        }
        val qrCode = QRCode(
            status = Status.Scanned,
            type = type,
            content = mainViewModel.result.value
        )
        Text(
            text = "Formatted result",
            fontWeight = FontWeight.W600,
            color = MaterialTheme.colors.TextColor,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
        )

        when (type) {
            QROptions.Text -> {
                TextItem(qrCode, false)
            }
            QROptions.URL -> {
                URLItem(qrCode, false)
            }
            QROptions.Email -> {
                EmailItem(qrCode, false)
            }
            QROptions.PhoneNumber -> {
                PhoneNumberItem(qrCode, false)
            }
            QROptions.SMS -> {
                SMSItem(qrCode, false)
            }
            QROptions.WiFi -> {
                WifiItem(qrCode, false)
            }
            QROptions.GeographicLocation -> {
                GeoGraphicItem(qrCode, false)
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
            // shows a traditional banner test ad
            AndroidView(
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = context.getString(R.string.ad_id_banner)
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            onClick = {
                mainViewModel.insertQRCode(
                    QRCodeEntity(
                        id = 0,
                        QRCode(
                            status = Status.Scanned,
                            type = type,
                            content = mainViewModel.result.value
                        )
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = MaterialTheme.colors.ButtonColor,
                contentColor = Color.White
            )
        ) {
            Row {
                Text(text = "Save QR Code")
            }
        }
    }

}