package com.helic.qrscan.core

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class MLKitBarcodeAnalyzer(
    private val onScanned: (result: String) -> Unit
) : ImageAnalysis.Analyzer {

    private var isScanning: Boolean = false

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null && !isScanning) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            // ...
            val scanner = BarcodeScanning.getClient()

            isScanning = true
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    // Task completed successfully
                    // ...

                    barcodes.firstOrNull().let { barcode ->
                        val rawValue = barcode?.rawValue
                        rawValue?.let {
                            Log.d("Barcode", it)
//                            mainViewModel.result.value = it
                            onScanned(it)
                        }
                    }
                    isScanning = false
                    imageProxy.close()
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                    isScanning = false
                    imageProxy.close()
                }
        }
    }
}