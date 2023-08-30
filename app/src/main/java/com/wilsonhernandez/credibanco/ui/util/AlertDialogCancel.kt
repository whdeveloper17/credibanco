package com.wilsonhernandez.credibanco.ui.util

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp

@Composable
fun AlertDialogCancel(
    onConfirmation: () -> Unit,
) {
    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(true) }
            if (openDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    text = {
                        Text("¿Esta seguro que desea anular la transacción?")
                    },
                    confirmButton = {
                        Button(

                            onClick = {
                                onConfirmation()
                            }) {
                            Text("Si")
                        }
                    },
                    dismissButton = {
                        Button(

                            onClick = {
                                openDialog.value=false
                            }) {
                            Text("No")
                        }
                    }

                    )
            }
        }

    }
}