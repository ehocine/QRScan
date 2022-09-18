package com.helic.qrscan.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.helic.qrscan.R
import com.helic.qrscan.components.NoResults
import com.helic.qrscan.components.QRCodeItem
import com.helic.qrscan.components.TopBar
import com.helic.qrscan.navigation.Screens
import com.helic.qrscan.ui.theme.BackgroundColor
import com.helic.qrscan.ui.theme.ButtonColor
import com.helic.qrscan.ui.theme.TextColor
import com.helic.qrscan.utils.ButtonOptions
import com.helic.qrscan.utils.DisplayAlertDialog
import com.helic.qrscan.viewmodel.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {

    val qrCodeList by mainViewModel.qrCodeEntityList.collectAsState()
    LaunchedEffect(key1 = true) {
        mainViewModel.readQRCodes()
    }
    Scaffold(
        floatingActionButton = {
            ButtonDropMenu {
                when (it) {
                    ButtonOptions.Scan -> {
                        if (mainViewModel.checkCameraPermission()) {
                            navController.navigate(Screens.ScanQRCode.route) {
                            }
                        } else {
                            snackbar("Camera unavailable", SnackbarDuration.Short)
                        }
                    }
                    else -> {
                        navController.navigate(Screens.GenerateQRScreen.route) {
                        }
                    }
                }
            }
        },
        bottomBar = {
            Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.BottomCenter) {
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
        }
    ) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.BackgroundColor) {
            Column(Modifier.fillMaxSize()) {
                TopBar()
                Spacer(modifier = Modifier.height(8.dp))
                if (qrCodeList.isEmpty()) {
                    NoResults()
                } else {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Here are your QR codes",
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.TextColor,
                            modifier = Modifier.weight(9f)
                        )
                        DeleteAllDropDown(mainViewModel = mainViewModel)
                    }
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(qrCodeList) { qrCodeEntity ->
                            QRCodeItem(
                                qrCode = qrCodeEntity.qrCode,
                                onItemClicked = {
                                    mainViewModel.selectedQRCodeEntity.value = qrCodeEntity
                                    navController.navigate(Screens.Details.route)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonDropMenu(chosenOption: (option: ButtonOptions) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    FloatingActionButton(
        onClick = { expanded = true },
        backgroundColor = MaterialTheme.colors.ButtonColor,
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            tint = Color.White,
            contentDescription = ""
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                expanded = false
                chosenOption(ButtonOptions.Scan)
            }) {
                Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = "")
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Scan QR code",
                    modifier = Modifier.padding(start = 5.dp),
                )
            }
            DropdownMenuItem(onClick = {
                expanded = false
                chosenOption(ButtonOptions.Generate)
            }) {
                Icon(imageVector = Icons.Default.QrCode2, contentDescription = "")
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Generate QR code",
                    modifier = Modifier.padding(start = 5.dp),
                )
            }
        }
    }
}

@Composable
fun DeleteAllDropDown(mainViewModel: MainViewModel) {
    var openDeleteDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = MaterialTheme.colors.TextColor
        )
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colors.BackgroundColor),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                expanded = false
                openDeleteDialog = true
            }) {
                Row(Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete All QR codes",
                        tint = MaterialTheme.colors.TextColor
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = "Delete All QR codes",
                        modifier = Modifier.padding(start = 5.dp),
                        color = MaterialTheme.colors.TextColor
                    )
                }
            }
        }
    }
    DisplayAlertDialog(
        title = "Delete all the QR Codes",
        message = {
            Text(
                text = "Do you want to delete all your QR codes?",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.TextColor
            )
        },
        openDialog = openDeleteDialog,
        closeDialog = { openDeleteDialog = false },
        onYesClicked = {
            mainViewModel.deleteAllQRCode()
        })

}

