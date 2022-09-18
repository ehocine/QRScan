package com.helic.qrscan.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.GeneratingTokens
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screens(route = "home", title = "Home", icon = Icons.Default.Home)
    object ScanQRCode :
        Screens(route = "scan_qr_screen", title = "Scan QR", icon = Icons.Default.Scanner)

    object GenerateQRScreen : Screens(
        route = "generate_qr_screen",
        title = "Generate QR",
        icon = Icons.Default.GeneratingTokens
    )

    object Details : Screens(
        route = "details",
        title = "Title",
        icon = Icons.Default.Details
    )
}
