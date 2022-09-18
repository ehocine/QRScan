package com.helic.qrscan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.helic.qrscan.R
import com.helic.qrscan.ui.theme.BackgroundColor
import com.helic.qrscan.ui.theme.MediumGray


@Composable
fun NoResults() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.BackgroundColor),
//            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MyLottieAnimation(modifier = Modifier.size(200.dp), lottie = R.raw.empty_state)
        Title(title = "No QR Codes")

    }
}