package com.helic.qrscan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.helic.qrscan.ui.theme.BackgroundColor
import com.helic.qrscan.ui.theme.TextColor

@Composable
fun TopBar() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Hey there",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.TextColor
            )
        }
    }
}