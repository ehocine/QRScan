package com.helic.qrscan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.MobileAds
import com.helic.qrscan.navigation.RootNavGraph
import com.helic.qrscan.ui.theme.BackgroundColor
import com.helic.qrscan.ui.theme.QRScanTheme
import com.helic.qrscan.utils.Msnackbar
import com.helic.qrscan.utils.loadInterstitial
import com.helic.qrscan.utils.rememberSnackbarState
import com.helic.qrscan.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    private val mainViewModel: MainViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialize the mobile ads sdk
        MobileAds.initialize(this) {}

        setContent {
            QRScanTheme {
                val systemUiController = rememberSystemUiController()
                navController = rememberAnimatedNavController()
                val appState: Msnackbar = rememberSnackbarState()
                val systemUIColor = MaterialTheme.colors.BackgroundColor
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = systemUIColor
                    )
                }
                Scaffold(
                    scaffoldState = appState.scaffoldState
                ) {
                    RootNavGraph(
                        navController = navController,
                        mainViewModel = mainViewModel
                    ) { message, duration ->
                        appState.showSnackbar(message = message, duration = duration)
                    }
                }

            }
            prepareCameraPermission()
        }
        loadInterstitial(this)
    }

    private fun prepareCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestSinglePermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestSinglePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private val requestSinglePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Handle Permission granted/rejected
            if (!isGranted) {
                Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show()
            }
        }


}