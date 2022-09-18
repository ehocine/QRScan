package com.helic.qrscan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.helic.qrscan.data.model.Status
import com.helic.qrscan.ui.theme.statusBlueColor
import com.helic.qrscan.ui.theme.statusRedColor

@Composable
fun StatusTag(status: Status) {
    val color = if (status == Status.Scanned) statusBlueColor else statusRedColor
    ChipView(status = status, color = color)
}

@Composable
fun ChipView(status: Status, color: Color) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(.08f))
    ) {
        Text(
            text = status.toString(), modifier = Modifier.padding(12.dp, 6.dp, 12.dp, 6.dp),
            style = MaterialTheme.typography.caption,
            color = color
        )
    }
}