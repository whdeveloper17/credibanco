package com.wilsonhernandez.credibanco.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wilsonhernandez.credibanco.R

@Composable
fun AlertDialogInternet(
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
                        Text(text = "Sin conexion a internet", fontSize = 16.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    },
                    text = {
                       Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                           Image(modifier = Modifier.size(120.dp) , painter = painterResource(id = R.drawable.no_wifi), contentDescription ="Sin conexion a internet" )
                       }
                    },
                    confirmButton = {
                        Button(

                            onClick = {
                                onConfirmation()
                            }) {
                            Text("Aceptar")
                        }
                    },

                    )
            }
        }

    }
}
@Composable
@Preview
fun AlertDialogInternetPreview(){
    AlertDialogInternet(){}
}
