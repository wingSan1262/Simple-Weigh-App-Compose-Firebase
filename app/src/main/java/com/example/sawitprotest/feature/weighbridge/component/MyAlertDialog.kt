package com.example.sawitprotest.feature.weighbridge.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true)
fun MyAlertDialog(
    title : String = "Alert",
    message : String = "Are you sure?",
    onClose: () -> Unit = {},
    onAccept : () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {onClose()},
        title = {
            Text(text = title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAccept()
                    onClose()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClose()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}