package com.wilsonhernandez.credibanco.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun AlertDialogDetail(
    receiptId: String, rrn: String,
    statusCode: String, statusDescription: String,status:Boolean,
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
                    title = {
                        Text(text = "Detalle de la transacción", fontSize = 18.sp)
                    },
                    text = {
                        Column {
                            Column {
                                Text(text = "Id de recibo:", fontWeight = FontWeight.Bold)
                                Text(text = receiptId)
                            }
                            Column {
                                Text(text = "Id de transacción: ", fontWeight = FontWeight.Bold)
                                Text(text = rrn)
                            }
                            Row {
                                Text(text = "Codigo de estado: ", fontWeight = FontWeight.Bold)
                                Text(text = statusCode)
                            }
                            Row {
                                Text(text = "Descripción : ", fontWeight = FontWeight.Bold)
                                Text(text = statusDescription)
                            }
                            Row {
                                Text(text = "Anulada : ", fontWeight = FontWeight.Bold)
                                Text(text = if (status) "No" else "Si")
                            }
                        }
                    },
                    confirmButton = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    openDialog.value = false
                                    onConfirmation.invoke()
                                }) {
                                Text("Aceptar")
                            }
                        }
                    },

                    )
            }
        }

    }
}