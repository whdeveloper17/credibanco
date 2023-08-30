import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.wilsonhernandez.credibanco.R
import com.wilsonhernandez.credibanco.authorization.ui.AuthorizationViewModel
import com.wilsonhernandez.credibanco.core.room.AuthorizationDao
import com.wilsonhernandez.credibanco.ui.theme.Purple40
import com.wilsonhernandez.credibanco.ui.theme.Purple80
import com.wilsonhernandez.credibanco.ui.util.insertDecimal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorizationScreen(dao: AuthorizationDao?, onclickButtonBack: () -> Unit) {

    val viewModel = AuthorizationViewModel(dao!!)
    val snackbarHostSate = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    val id: String by viewModel.id.observeAsState(initial = "")
    val commerceCode: String by viewModel.commerceCode.observeAsState(initial = "")
    val terminalCode: String by viewModel.terminalCode.observeAsState(initial = "")
    val amount: String by viewModel.amount.observeAsState(initial = "")
    val card: String by viewModel.card.observeAsState(initial = "")
    val enable: Boolean by viewModel.isAuthorizationEnable.observeAsState(initial = false)
    val success: Boolean by viewModel.succesAuthorization.observeAsState(initial = false)
    val snackbar: String by viewModel.isSnackbar.observeAsState(initial = "")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostSate)
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onclickButtonBack.invoke() }) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Atras")
                    }
                },
                title = {
                    Text(text = "Autorización ")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp)
        ) {
            Text(
                text = "Formulario para la Autorización de transacciones ",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontFamily = FontFamily.SansSerif,
                fontSize = 16.sp
            )
            TextFieldUid(uid = id) {
                viewModel.onAuthorizationChanged(it, commerceCode, terminalCode, amount, card)
            }
            TextFieldCommerce(commerce = commerceCode) {
                viewModel.onAuthorizationChanged(id, it, terminalCode, amount, card)
            }
            TextFieldTerminal(terminal = terminalCode) {
                viewModel.onAuthorizationChanged(id, commerceCode, it, amount, card)
            }
            TextFieldAmount(amount = amount) {
                viewModel.onAuthorizationChanged(id, commerceCode, terminalCode, it, card)
            }
            TextFieldCard(card = card) {
                viewModel.onAuthorizationChanged(id, commerceCode, terminalCode, amount, it)
            }
            Spacer(modifier = Modifier.height(50.dp))
            ButtonAction(enabled = enable) {
                viewModel.onAuthorization()
            }

            val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
            BlockingCircularProgress(isLoading)

            if (!snackbar.isNullOrEmpty()){
                scope.launch {
                    viewModel.clearSnackbar()
                    snackbarHostSate.showSnackbar(message = snackbar)
                }
            }
            if (success){

                AlertDialogInformation(title = "Solictud Exitosa", message = "Autorización de una Transacción con exito.") {
                    viewModel.clearSuccesAuthorization()
                    onclickButtonBack.invoke()
                }
            }


        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AuthorizationScreenPreview() {
    AuthorizationScreen(null,{})
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldUid(uid: String, onTextChanged: (String) -> Unit) {
    val openAlertDialog = remember { mutableStateOf(false) }
    OutlinedTextField(
        value = uid,
        onValueChange = { onTextChanged.invoke(it) },
        label = { Text("UID") },
        maxLines = 1,
        enabled = true,
        textStyle = TextStyle(color = Color.Black),

        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_number),
                contentDescription = ""
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                openAlertDialog.value = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = ""
                )
            }
        },
        modifier = Modifier.padding(start = 10.dp, top = 10.dp)
    )

    when {
        openAlertDialog.value -> {
            AlertDialogInformation(
                title = "Informacion de campo UID",
                message = "Este campo sera por default con el UID del telefono donde se este usando la aplicacion",
                onConfirmation = {
                    openAlertDialog.value = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldCommerce(commerce: String, onTextChanged: (String) -> Unit) {
    val openAlertDialog = remember { mutableStateOf(false) }
    OutlinedTextField(
        value = commerce,
        onValueChange = { onTextChanged.invoke(it) },
        label = { Text("Codigo comercio") },
        maxLines = 1,
        textStyle = TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_local),
                contentDescription = ""
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                openAlertDialog.value = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = ""
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
    )

    when {
        openAlertDialog.value -> {
            AlertDialogInformation(
                title = "Informacion de campo codigo comercio",
                message = "Es el código del comercio en donde estará instalado el dispositivo móvil, para efectos de esta prueba siempre debe ser: 000123 ",
                onConfirmation = {
                    openAlertDialog.value = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldTerminal(terminal: String, onTextChanged: (String) -> Unit) {
    val openAlertDialog = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = terminal,
        onValueChange = { onTextChanged.invoke(it) },
        label = { Text("Codigo terminal") },
        maxLines = 1,
        textStyle = TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_number),
                contentDescription = ""
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                openAlertDialog.value = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = ""
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
    )

    when {
        openAlertDialog.value -> {
            AlertDialogInformation(
                title = "Informacion de campo Codigo terminal",
                message = "Es el número que identificará al dispositivo móvil en el comercio, para efectos de esta prueba siempre debe ser: 000ABC ",
                onConfirmation = {
                    openAlertDialog.value = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TextFieldAmount(amount: String, onTextChanged: (String) -> Unit) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val openKeyboard = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = amount,
                selection = TextRange(amount.length)
            )
        )
    }
    OutlinedTextField(
        value = textFieldValueState,
        onValueChange = {
            if (it.text.length <= 10) {
                onTextChanged.invoke(it.text)
                textFieldValueState = if (!it.text.isNullOrEmpty()) {
                    val value = insertDecimal(it.text.replace(".", "").toInt())
                    TextFieldValue(

                        text = value,
                        selection = TextRange(value.length)
                    )
                } else {

                    TextFieldValue(
                        text = "",
                        selection = TextRange(it.text.length)
                    )
                }
            }
        },
        label = { Text("Cantidad") },
        maxLines = 1,
        textStyle = TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_money),
                contentDescription = ""
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                openAlertDialog.value = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = ""
                )
            }
        },
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                keyboardController?.show()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
    )

    when {
        openAlertDialog.value -> {
            AlertDialogInformation(
                title = "Informacion de campo cantidad",
                message = "Es el valor total a pagar por la transacción, para efectos de esta prueba ese valor será una cadena de caracteres en donde los dos últimos\n" +
                        "caracteres (a la derecha) serán los dígitos decimales, los demás números (a la izquierda) serán la parte entera del valor total a pagar. Por ejemplo: El monto ciento veintitrés pesos con cuarenta y cinco centavos (\$123.45) será representado de esta manera: “12345”.",
                onConfirmation = {
                    openAlertDialog.value = false
                }
            )
        }

        openKeyboard.value -> {
            keyboardController?.hide()
            keyboardController?.show()
            openKeyboard.value = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldCard(card: String, onTextChanged: (String) -> Unit) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val formattedValue = buildString {
        val digits = card.filter { it.isDigit() }
        for (i in digits.indices) {
            append(digits[i])
            if ((i + 1) % 4 == 0 && i != digits.lastIndex) {
                append(" ")
            }
        }
    }
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = formattedValue,
                selection = TextRange(formattedValue.length)
            )
        )
    }
    OutlinedTextField(
        value = textFieldValueState,
        onValueChange = {
            val newValue = it.text.filter { it.isDigit() }
            if (newValue.length <= 16) {
                val formattedValue = buildString {
                    val digits = it.text.filter { it.isDigit() }
                    for (i in digits.indices) {
                        append(digits[i])
                        if ((i + 1) % 4 == 0 && i != digits.lastIndex) {
                            append(" ")
                        }
                    }
                }
                onTextChanged.invoke(newValue)
                textFieldValueState = if (!it.text.isNullOrEmpty()) {
                    TextFieldValue(
                        text = formattedValue,
                        selection = TextRange(formattedValue.length)
                    )
                } else {

                    TextFieldValue(
                        text = "",
                        selection = TextRange(it.text.length)
                    )
                }
            }
        },
        label = { Text("Tarjeta") },
        maxLines = 1,
        textStyle = TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_card),
                contentDescription = ""
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                openAlertDialog.value = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = ""
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
    )

    when {
        openAlertDialog.value -> {
            AlertDialogInformation(
                title = "Informacion de campo Tarjeta",
                message = "Será el número de la tarjeta con la cual se realizará el pago, para efectos de esta prueba siempre debe ser: 1234567890123456",
                onConfirmation = {
                    openAlertDialog.value = false
                }
            )
        }
    }
}

@Composable
fun ButtonAction(enabled: Boolean, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = { onClick.invoke() }, enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple40,
                disabledContainerColor = Purple80
            ),
        )

        {
            Text(text = "Generar tranzacion", color = Color.White)
        }
    }

}

@Composable
fun BlockingCircularProgress(isBlocking: Boolean) {
    if (isBlocking) {
      var isShowingDialog by remember { mutableStateOf(true) }

        DisposableEffect(isBlocking) {
            onDispose {
                isShowingDialog = false
            }
        }

        if (isShowingDialog) {
            Dialog(
                onDismissRequest = { /* Handle dialog dismissal if needed */ },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


