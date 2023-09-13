import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wilsonhernandez.credibanco.data.database.entities.TransactionsEntity
import com.wilsonhernandez.credibanco.theme.Purple40
import com.wilsonhernandez.credibanco.theme.Purple80
import com.wilsonhernandez.credibanco.ui.viewmodel.SearchViewModel
import com.wilsonhernandez.credibanco.util.AlertDialogCancel
import com.wilsonhernandez.credibanco.util.AlertDialogDetail
import com.wilsonhernandez.credibanco.util.AlertDialogInternet
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, onclickButtonBack: () -> Unit) {
    val state = remember {
        mutableStateOf(false)
    }
    val snackbarHostSate = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    val snackbar: String by viewModel.isSnackbar.observeAsState(initial = "")
    DisposableEffect(Unit) {
        viewModel.getList()
        onDispose { }
    }
    val listState: State<List<TransactionsEntity>?> = viewModel.listTransaction.observeAsState()
    val list: List<TransactionsEntity> = listState.value ?: emptyList()
    val alertInternet: Boolean by viewModel.alertInternet.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostSate)
        },
        topBar = {


            if (!state.value) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            onclickButtonBack.invoke()
                            viewModel.getListFilter("")
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Atras"
                            )
                        }

                    },
                    actions = {
                        IconButtonSearch {
                            state.value = true
                        }
                    },
                    title = {
                        Text(text = "Buscar transacciónes ")
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Purple40,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                )
            } else {
                SearchBar(onSearch = { viewModel.getListFilter(it) }, onClick = {
                    state.value = false
                }, onClickClean = {
                    viewModel.getListFilter("")
                })
            }
        },
    ) {
        if (list.isNotEmpty()) {
            LazyColumn(modifier = Modifier.padding(top = 65.dp)) {
                items(list.size) { index ->
                    itemListSearch(item = list[index], onclickCancel = {
                        viewModel.onCancelAuthorization(list[index])
                    })
                }
            }
        }else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(
                    text = "No hay transacción",
                )
            }
        }


    }


    BlockingCircularProgress(isLoading)

    if (alertInternet){
        AlertDialogInternet {
            viewModel.enableAlertInternet()
        }
    }
    if (!snackbar.isNullOrEmpty()){
        scope.launch {
            viewModel.clearSnackbar()
            snackbarHostSate.showSnackbar(message = snackbar)
        }
    }
}

@Composable
fun IconButtonSearch(onClick: () -> Unit) {
    IconButton(onClick = { onClick.invoke() }) {
        Icon(imageVector = Icons.Outlined.Search, contentDescription = "Buscar")
    }
}

@Composable
fun itemListSearch(item: TransactionsEntity, onclickCancel:()->Unit) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val openAlertDialogCancel = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(120.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(120.dp)
                .background(color = Color.White)
        ) {
            Text(text = item.id.toString())
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(text = "Id de recibo:", fontWeight = FontWeight.Bold)
                Text(text = item.receiptId)
                Row {
                    Text(text = "Anulada : ", fontWeight = FontWeight.Bold)
                    Text(text = if (item.state) "No" else "Si")

                }
                Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxWidth()) {
                    Row {
                        IconButton(onClick = { openAlertDialog.value = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "Informacion",
                                tint = Color.Blue,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        if (item.state) {
                            IconButton(onClick = { openAlertDialogCancel.value=true}) {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = "Anular",
                                    tint = Color.Red,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }
                }
            }

        }
    }
    if (openAlertDialog.value) {
        AlertDialogDetail(
            receiptId = item.receiptId,
            rrn = item.rrn,
            statusCode = item.statusCode,
            statusDescription = item.statusDescription, status = item.state
        ) {
            openAlertDialog.value = false
        }
    }
    if (openAlertDialogCancel.value){
        AlertDialogCancel {
            openAlertDialogCancel.value=false
            onclickCancel.invoke()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
    onClick: () -> Unit,
    onClickClean: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = Purple80
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Atras",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onClick.invoke() }
            )

            Spacer(modifier = Modifier.width(16.dp))

            BasicTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    onSearch.invoke(it)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        view.clearFocus()

                    }
                ),
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                singleLine = true,
                modifier = Modifier
                    .width(screenWidthDp - 100.dp)
                    .padding(end = 10.dp)
            )
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Borrar",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        searchText = ""
                        onClickClean.invoke()
                    }
            )

        }
    }
}