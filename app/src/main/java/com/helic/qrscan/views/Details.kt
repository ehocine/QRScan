package com.helic.qrscan.views

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.helic.qrscan.R
import com.helic.qrscan.components.*
import com.helic.qrscan.data.model.QRCode
import com.helic.qrscan.ui.theme.BackgroundColor
import com.helic.qrscan.ui.theme.ButtonColor
import com.helic.qrscan.ui.theme.TextColor
import com.helic.qrscan.utils.DisplayAlertDialog
import com.helic.qrscan.utils.QROptions
import com.helic.qrscan.utils.showInterstitial
import com.helic.qrscan.viewmodel.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Details(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    mainViewModel.generatedQRCode.value = null
    val context = LocalContext.current
    val selectedQRCodeEntity by mainViewModel.selectedQRCodeEntity
    var openDeleteDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                actions = {
                    IconButton(onClick = {
                        openDeleteDialog = true
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                    }
                },
                backgroundColor = MaterialTheme.colors.BackgroundColor,
                contentColor = MaterialTheme.colors.TextColor,
                elevation = 0.dp,
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp, 24.dp)
                            .clickable {
                                navController.navigateUp()
                            },
                        tint = MaterialTheme.colors.TextColor
                    )
                }
            )
        },
        content = {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.BackgroundColor) {
                DetailsView(
                    qrCode = selectedQRCodeEntity.qrCode,
                    mainViewModel = mainViewModel,
                    snackbar = snackbar
                )
                DisplayAlertDialog(
                    title = "Delete QR code",
                    message = {
                        Text(
                            text = "Do you really want to delete this QR code?",
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.TextColor
                        )
                    },
                    openDialog = openDeleteDialog,
                    closeDialog = { openDeleteDialog = false },
                    onYesClicked = {
                        mainViewModel.deleteQRCode(selectedQRCodeEntity)
                        navController.navigateUp()
                    })
            }
            showInterstitial(context)
        }
    )
}

@Composable
fun DetailsView(
    qrCode: QRCode,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    val generatedQRCode: Bitmap? by mainViewModel.generatedQRCode
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
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
        }
        Spacer(modifier = Modifier.padding(25.dp))
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(generatedQRCode)
                .crossfade(true)
                .build(),
            contentDescription = "QR Code"
        ) {
            val state = painter.state
            if (state is AsyncImagePainter.State.Success) {
                SubcomposeAsyncImageContent(
                    modifier = Modifier.clip(RectangleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
        if (generatedQRCode != null) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = {
                    if (mainViewModel.saveQRCode(generatedQRCode)) snackbar(
                        "QR Code saved successfully",
                        SnackbarDuration.Short
                    ) else snackbar("Couldn\'t save the QR Code", SnackbarDuration.Short)
                }) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        tint = MaterialTheme.colors.TextColor,
                        contentDescription = "Save"
                    )
                }
            }
        }
        Box(
            Modifier
                .fillMaxHeight()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                Spacer(modifier = Modifier.padding(16.dp))
                Button(
                    onClick = { mainViewModel.generateQRCode(qrCode.content) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(16.dp, 0.dp, 16.dp, 0.dp),
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = MaterialTheme.colors.ButtonColor,
                        contentColor = Color.White
                    )
                ) {
                    Text("View QR Code")
                }
            }
        }
    }
}
