import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlertDialogInformation(
    title: String, message: String,
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
                        Text(text = title, fontSize = 16.sp)
                    },
                    text = {
                        Text(message)
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