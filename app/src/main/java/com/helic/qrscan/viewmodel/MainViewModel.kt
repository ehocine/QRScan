package com.helic.qrscan.viewmodel

import android.Manifest
import android.app.Application
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.helic.qrscan.data.database.QRCodeEntity
import com.helic.qrscan.data.repository.Repository
import com.helic.qrscan.utils.convertTimeStampToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    @OptIn(ExperimentalMaterialApi::class)
    val modalBottomSheetState: MutableState<ModalBottomSheetState> =
        mutableStateOf(ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden))

    val result: MutableState<String> = mutableStateOf("")

    val generatedQRCode: MutableState<Bitmap?> = mutableStateOf(null)

    val selectedQRCodeEntity: MutableState<QRCodeEntity> = mutableStateOf(QRCodeEntity())

    private var _qrCodeEntityList = MutableStateFlow<List<QRCodeEntity>>(emptyList())
    val qrCodeEntityList: StateFlow<List<QRCodeEntity>> = _qrCodeEntityList

    fun checkCameraPermission(): Boolean = ContextCompat.checkSelfPermission(
        getApplication(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    fun generateQRCode(text: String) {
        val height = 600
        val width = 600
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (i in 0 until width) {
                for (j in 0 until height) {
                    bitmap.setPixel(i, j, if (bitMatrix[i, j]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        generatedQRCode.value = bitmap
    }

    fun saveQRCode(bitmap: Bitmap?): Boolean {
        val imageCollection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val contentValues = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "${convertTimeStampToDate(System.currentTimeMillis())}.jpeg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (bitmap != null) {
                put(MediaStore.Images.Media.WIDTH, bitmap.width)
                put(MediaStore.Images.Media.HEIGHT, bitmap.height)
            }
        }
        return try {
            val context = getApplication<Application>()
            context.contentResolver.insert(imageCollection, contentValues)?.also {
                context.contentResolver.openOutputStream(it).use { outputStream ->
                    if (bitmap != null) {
                        if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                            throw IOException("Failed to save image")
                        }
                    }
                }
            } ?: throw IOException("Failed to create Media Store entry")
            true

        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun readQRCodes() {
        viewModelScope.launch {
            repository.readQRCode().collect {
                _qrCodeEntityList.emit(it)
            }
        }
    }

    fun insertQRCode(qrCodeEntity: QRCodeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertQRCode(qrCodeEntity)
        }

    fun deleteQRCode(qrCodeEntity: QRCodeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteQRCode(qrCodeEntity)
        }

    fun deleteAllQRCode() {
        viewModelScope.launch {
            repository.deleteAllQRCode()
        }
    }
}