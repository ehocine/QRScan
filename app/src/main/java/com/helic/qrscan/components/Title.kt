package com.helic.qrscan.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.helic.qrscan.ui.theme.TextColor

@Composable
fun Title(title: String) {
    Text(
        text = title,
        modifier = Modifier
//            .fillMaxWidth()
            .padding(16.dp, 0.dp, 0.dp, 0.dp),
        color = MaterialTheme.colors.TextColor,
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.W600,
        textAlign = TextAlign.Start
    )
}