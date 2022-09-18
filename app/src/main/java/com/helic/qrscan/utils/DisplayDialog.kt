package com.helic.qrscan.utils

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.helic.qrscan.ui.theme.ButtonColor
import com.helic.qrscan.ui.theme.DialogNoText

@Composable
fun DisplayAlertDialog(
    title: String,
    message: @Composable (() -> Unit),
    openDialog: Boolean,
    closeDialog: () -> Unit,
    onYesClicked: () -> Unit,
) {
    if (openDialog) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = message,
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.ButtonColor),
                    onClick = {
                        onYesClicked()
                        closeDialog()
                    })
                {
                    Text(text = "Yes", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { closeDialog() })
                {
                    Text(
                        text = "No",
                        color = MaterialTheme.colors.DialogNoText
                    )
                }
            },
            onDismissRequest = { closeDialog() }
        )
    }
}
