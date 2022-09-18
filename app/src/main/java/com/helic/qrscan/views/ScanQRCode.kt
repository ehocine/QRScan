package com.helic.qrscan.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.Size
import android.view.OrientationEventListener
import android.view.Surface
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.helic.qrscan.core.MLKitBarcodeAnalyzer
import com.helic.qrscan.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScanQRCode(
    navController: NavController,
    mainViewModel: MainViewModel
) {

    var firstTime = true
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val previewView = remember { PreviewView(context) }
    val lifeCycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val cameraSelector =
        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
    val cameraExecutor = Executors.newSingleThreadExecutor()
    val imageAnalysis = ImageAnalysis.Builder()
        .setTargetResolution(
            Size(
                previewView.width,
                previewView.height
            )
        )
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    val orientationEventListener = object : OrientationEventListener(context) {
        override fun onOrientationChanged(orientation: Int) {
            // Monitors orientation values to determine the target rotation value
            val rotation: Int = when (orientation) {
                in 45..134 -> Surface.ROTATION_270
                in 135..224 -> Surface.ROTATION_180
                in 225..314 -> Surface.ROTATION_90
                else -> Surface.ROTATION_0
            }

            imageAnalysis.targetRotation = rotation
        }
    }
    orientationEventListener.enable()

    val analyzer: ImageAnalysis.Analyzer = MLKitBarcodeAnalyzer {
        imageAnalysis.clearAnalyzer()
        mainViewModel.result.value = it
        scope.launch {
            mainViewModel.modalBottomSheetState.value.animateTo(ModalBottomSheetValue.Expanded)
        }
    }

    preview.setSurfaceProvider(previewView.surfaceProvider)

    var camera: Camera? by remember { mutableStateOf(null) }

    LaunchedEffect(
        key1 = mainViewModel.modalBottomSheetState.value.currentValue == ModalBottomSheetValue.Hidden
    ) {
        if (mainViewModel.modalBottomSheetState.value.currentValue == ModalBottomSheetValue.Hidden) {
            imageAnalysis.setAnalyzer(cameraExecutor, analyzer)
            val cameraProvider = context.getCameraProvider()
            cameraProvider.unbindAll()
            firstTime = false
            camera = cameraProvider.bindToLifecycle(
                lifeCycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        }
    }
    val torchState by remember { mutableStateOf(TorchState.OFF) }
    var isFlashOn by remember { mutableStateOf(false) }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        sheetState = mainViewModel.modalBottomSheetState.value,
        sheetContent = {
            ResultScreen(mainViewModel = mainViewModel)
        }
    ) {
        Scaffold {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {

                AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
                CameraOverlay()
                if (camera != null) {
                    if (camera?.cameraInfo?.hasFlashUnit() == true) {
                        Log.d("Tag", "Found")
                        IconButton(onClick = { camera!!.cameraControl.enableTorch(!isFlashOn) }) {
                            Icon(
                                imageVector = when (torchState) {
                                    TorchState.ON -> {
                                        isFlashOn = true
                                        Icons.Default.FlashOff
                                    }
                                    else -> {
                                        isFlashOn = false
                                        Icons.Default.FlashOn
                                    }
                                },
                                tint = Color.White,
                                contentDescription = ""
                            )
                        }
                    } else {
                        Log.d("Tag", "No flash $camera")
                        Log.d("Tag", "No flash")
                    }
                } else {
                    Log.d("Tag", "Camera null")
                }
            }
        }
    }
}

@Composable
fun CameraOverlay() {
    Box(
        Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            val rectPath = Path().apply {
                addRect(Rect(center, size.minDimension / 3))
            }
            clipPath(rectPath, clipOp = ClipOp.Difference) {
                drawRect(SolidColor(Color.Black.copy(alpha = 0.8f)))
            }
        })
    }
}


private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also {
            it.addListener(
                {
                    continuation.resume(it.get())
                }, ContextCompat.getMainExecutor(this)
            )
        }
    }