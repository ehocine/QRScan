package com.helic.qrscan.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.helic.qrscan.data.model.QRCode
import com.helic.qrscan.ui.theme.TextColor


@Composable
fun TextItem(qrCode: QRCode, showStatusTag: Boolean) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(8f)
        ) {
            Text(
                text = qrCode.type.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = MaterialTheme.colors.TextColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = qrCode.content,
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (showStatusTag) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                horizontalArrangement = Arrangement.End
            ) {
                StatusTag(qrCode.status)
            }
        }
    }
}

@Composable
fun URLItem(qrCode: QRCode, showStatusTag: Boolean) {

    val urlContent = if (qrCode.content.startsWith(
            "URLTO:",
            ignoreCase = true
        )
    ) qrCode.content.substring(6) else qrCode.content
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(8f)
        ) {
            Text(
                text = qrCode.type.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = MaterialTheme.colors.TextColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = urlContent,
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (showStatusTag) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                horizontalArrangement = Arrangement.End
            ) {
                StatusTag(qrCode.status)
            }
        }
    }
}

@Composable
fun EmailItem(qrCode: QRCode, showStatusTag: Boolean) {

    val tokens = qrCode.content.split("?", "&", ignoreCase = true, limit = 5)
    var email = ""
    var subject = ""
    var message = ""

    for (i in tokens.indices) {
        if (tokens[i].startsWith("mailto:")) email = tokens[i].substring(7)
        if (tokens[i].startsWith("subject=")) subject = tokens[i].substring(8)
        if (tokens[i].startsWith("body=")) message = tokens[i].substring(5)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(8f)
        ) {
            Text(
                text = qrCode.type.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = MaterialTheme.colors.TextColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = "Email: $email",
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Subject: $subject",
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Message: $message",
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (showStatusTag) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                horizontalArrangement = Arrangement.End
            ) {
                StatusTag(qrCode.status)
            }
        }
    }
}

@Composable
fun PhoneNumberItem(qrCode: QRCode, showStatusTag: Boolean) {

    val phoneNumber = if (qrCode.content.startsWith(
            "tel:",
            ignoreCase = true
        )
    ) qrCode.content.substring(4) else ""
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(8f)
        ) {
            Text(
                text = qrCode.type.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = MaterialTheme.colors.TextColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = phoneNumber,
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (showStatusTag) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                horizontalArrangement = Arrangement.End
            ) {
                StatusTag(qrCode.status)
            }
        }
    }
}

@Composable
fun SMSItem(qrCode: QRCode, showStatusTag: Boolean) {
    val tokens = qrCode.content.split(":", ignoreCase = true, limit = 3)
    val phoneNumber = tokens[1]
    val message = tokens[2]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(8f)
        ) {
            Text(
                text = qrCode.type.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = MaterialTheme.colors.TextColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = "Phone number: $phoneNumber",
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Message: $message",
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (showStatusTag) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                horizontalArrangement = Arrangement.End
            ) {
                StatusTag(qrCode.status)
            }
        }
    }

}

@Composable
fun WifiItem(qrCode: QRCode, showStatusTag: Boolean) {

    val tokens = qrCode.content.split(";", ignoreCase = true, limit = 5)
    var encryption = ""
    var networkame = ""
    var password = ""

    for (i in tokens.indices) {
        if (tokens[i].startsWith("WIFI:T:")) encryption = tokens[i].substring(7)
        if (tokens[i].startsWith("S:")) networkame = tokens[i].substring(2)
        if (tokens[i].startsWith("P:")) password = tokens[i].substring(2)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(8f)
        ) {
            Text(
                text = qrCode.type.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = MaterialTheme.colors.TextColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = "Network name: $networkame",
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Encryption: $encryption",
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Password: $password",
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (showStatusTag) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                horizontalArrangement = Arrangement.End
            ) {
                StatusTag(qrCode.status)
            }
        }
    }
}

@Composable
fun GeoGraphicItem(qrCode: QRCode, showStatusTag: Boolean) {
    val tokens = qrCode.content.split(",", ignoreCase = true, limit = 2)

    var latitude = ""
    val longitude = tokens[1]

    for (i in tokens.indices) {
        if (tokens[i].startsWith("geo:")) latitude = tokens[i].substring(4)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(8f)
        ) {
            Text(
                text = qrCode.type.toString(),
                modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                color = MaterialTheme.colors.TextColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Text(
                    text = "Latitude: $latitude",
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Longitude: $longitude",
                    color = MaterialTheme.colors.TextColor,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (showStatusTag) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                horizontalArrangement = Arrangement.End
            ) {
                StatusTag(qrCode.status)
            }
        }
    }
}