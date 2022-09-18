package com.helic.qrscan.views

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.helic.qrscan.components.DropDownOptions
import com.helic.qrscan.components.DropDownQROptions
import com.helic.qrscan.components.Title
import com.helic.qrscan.data.database.QRCodeEntity
import com.helic.qrscan.data.model.QRCode
import com.helic.qrscan.data.model.Status
import com.helic.qrscan.ui.theme.BackgroundColor
import com.helic.qrscan.ui.theme.ButtonColor
import com.helic.qrscan.ui.theme.TextColor
import com.helic.qrscan.utils.QROptions
import com.helic.qrscan.utils.showInterstitial
import com.helic.qrscan.viewmodel.MainViewModel
import java.util.regex.Pattern

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GenerateQRScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    snackbar: (String, SnackbarDuration) -> Unit
) {
    val context = LocalContext.current
    var textToQR by remember { mutableStateOf("") }
    var qrOption by remember { mutableStateOf(QROptions.Text) }
    val optionsList =
        listOf(
            QROptions.Text,
            QROptions.URL,
            QROptions.Email,
            QROptions.PhoneNumber,
            QROptions.SMS,
            QROptions.WiFi,
            QROptions.GeographicLocation
        )

    mainViewModel.generatedQRCode.value = null
    val generatedQRCode: Bitmap? by mainViewModel.generatedQRCode
    var auth by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Generate QR Code") },
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
        }) {
        showInterstitial(context)
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.BackgroundColor) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Title(title = "Choose an option")
                    DropDownQROptions(
                        optionsList = optionsList
                    ) {
                        qrOption = it
                        auth = false
                        mainViewModel.generatedQRCode.value = null
                    }
                }

                when (qrOption) {
                    QROptions.Text -> {
                        TextQRCode(textToQR = { textToQR = it }, authorized = { auth = it })
                    }
                    QROptions.URL -> {
                        URLQRCode(textToQR = { textToQR = it }, authorized = { auth = it })
                    }
                    QROptions.Email -> {
                        EmailQRCode(textToQR = { textToQR = it }, authorized = {
                            auth = it
                        })
                    }
                    QROptions.PhoneNumber -> {
                        PhoneNumberQRCode(textToQR = { textToQR = it }, authorized = { auth = it })
                    }
                    QROptions.SMS -> {
                        SMSQRCode(textToQR = { textToQR = it }, authorized = { auth = it })
                    }
                    QROptions.WiFi -> {
                        WiFiQRCode(textToQR = { textToQR = it }, authorized = { auth = it })
                    }
                    QROptions.GeographicLocation -> {
                        GeographicLocationQRCode(
                            textToQR = { textToQR = it },
                            authorized = { auth = it })
                    }
                }
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
                    Button(
                        onClick = {
                            if (auth) {
                                mainViewModel.generateQRCode(textToQR)
                                mainViewModel.insertQRCode(
                                    QRCodeEntity(
                                        id = 0,
                                        QRCode(
                                            status = Status.Generated,
                                            type = qrOption,
                                            content = textToQR
                                        )
                                    )
                                )
                            } else {
                                snackbar(
                                    "Error: cannot create QR code, please check your inputs",
                                    SnackbarDuration.Short
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .padding(16.dp, 0.dp, 16.dp, 0.dp),
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = MaterialTheme.colors.ButtonColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Generate")
                    }
                }
            }
        }
    }
}

@Composable
fun TextQRCode(textToQR: (text: String) -> Unit, authorized: (auth: Boolean) -> Unit) {
    var text by remember { mutableStateOf("") }
    Column() {

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = {
                Text(
                    text = "Text",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "Text",
                    color = MaterialTheme.colors.TextColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            )
        )

    }
    authorized(text.isNotEmpty())
    textToQR(text)
}

@Composable
fun URLQRCode(textToQR: (text: String) -> Unit, authorized: (auth: Boolean) -> Unit) {
    var url by remember { mutableStateOf("") }
    Column() {

        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = {
                Text(
                    text = "URL",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "URL",
                    color = MaterialTheme.colors.TextColor
                )
            },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            )
        )

    }
    authorized(url.isNotEmpty())
    textToQR("URLTO:$url")
}


@Composable
fun EmailQRCode(textToQR: (text: String) -> Unit, authorized: (auth: Boolean) -> Unit) {

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    Column() {

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    text = "Email",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "Email",
                    color = MaterialTheme.colors.TextColor
                )
            },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = {
                Text(
                    text = "Subject",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "Subject",
                    color = MaterialTheme.colors.TextColor
                )
            },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = {
                Text(
                    text = "Message",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "Message",
                    color = MaterialTheme.colors.TextColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            )
        )
    }
    authorized(EMAIL_ADDRESS_PATTERN.matcher(email).matches())
    textToQR("mailto:$email?cc=&bcc=&subject=$subject&body=$message")
}


@Composable
fun PhoneNumberQRCode(textToQR: (text: String) -> Unit, authorized: (auth: Boolean) -> Unit) {
    var phoneNumber by remember { mutableStateOf("") }
    Column() {

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = {
                Text(
                    text = "Phone Number",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "Phone Number",
                    color = MaterialTheme.colors.TextColor
                )
            },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
    }
    authorized(phoneNumber.isNotEmpty())
    textToQR("tel:$phoneNumber")
}


@Composable
fun SMSQRCode(textToQR: (text: String) -> Unit, authorized: (auth: Boolean) -> Unit) {
    var phoneNumber by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    Column() {

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = {
                Text(
                    text = "Phone Number",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "Phone Number",
                    color = MaterialTheme.colors.TextColor
                )
            },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = {
                Text(
                    text = "Message",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "Message",
                    color = MaterialTheme.colors.TextColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            )
        )
    }
    authorized(phoneNumber.isNotEmpty())
    textToQR("sms:$phoneNumber:$message")
}

@Composable
fun WiFiQRCode(textToQR: (text: String) -> Unit, authorized: (auth: Boolean) -> Unit) {
    var networkName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var encryption by remember { mutableStateOf("") }
    val encryptionList = listOf("None", "WPA/WPA2", "WEP")
    Column() {

        OutlinedTextField(
            value = networkName,
            onValueChange = { networkName = it },
            label = {
                Text(
                    text = "Network Name",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "Network Name",
                    color = MaterialTheme.colors.TextColor
                )
            },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        DropDownOptions(
            label = "Encryption",
            optionsList = encryptionList,
            onOptionSelected = {
                encryption = it
                if (it == "None") password = ""
            })
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    text = "Password",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "Password",
                    color = MaterialTheme.colors.TextColor
                )
            },
            maxLines = 1,
            singleLine = true,
            readOnly = encryption == "None",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            )
        )
    }
    authorized(networkName.isNotEmpty() && encryption.isNotEmpty())
    textToQR("WIFI:T:$encryption;S:$networkName;P:$password;;")
}


@Composable
fun GeographicLocationQRCode(
    textToQR: (text: String) -> Unit,
    authorized: (auth: Boolean) -> Unit
) {
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    Column() {

        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = {
                Text(
                    text = "Latitude",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "Latitude",
                    color = MaterialTheme.colors.TextColor
                )
            },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = {
                Text(
                    text = "Longitude",
                    color = MaterialTheme.colors.TextColor
                )
            },
            placeholder = {
                Text(
                    text = "Longitude",
                    color = MaterialTheme.colors.TextColor
                )
            },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.TextColor
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
    }
    authorized(latitude.isNotEmpty() && longitude.isNotEmpty())
    textToQR("geo:$latitude,$longitude")
}


