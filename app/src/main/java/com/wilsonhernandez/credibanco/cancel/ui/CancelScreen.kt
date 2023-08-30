import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wilsonhernandez.credibanco.cancel.ui.CancelViewModel
import com.wilsonhernandez.credibanco.core.room.AuthorizationDao
import com.wilsonhernandez.credibanco.core.room.entity.AuthorizationEntity
import com.wilsonhernandez.credibanco.ui.theme.Purple40
import com.wilsonhernandez.credibanco.ui.util.AlertDialogCancel
import com.wilsonhernandez.credibanco.ui.util.AlertDialogDetail
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelScreen(dao: AuthorizationDao?, onclickButtonBack: () -> Unit){
    val viewModel = CancelViewModel(dao!!)
    val snackbarHostSate = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    viewModel.getListStatusApproved()
    val snackbar: String by viewModel.isSnackbar.observeAsState(initial = "")
    val listState: State<List<AuthorizationEntity>?> = viewModel.listAuthorization.observeAsState()
    val list: List<AuthorizationEntity> = listState.value ?: emptyList()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onclickButtonBack.invoke() }) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Atras")
                    }
                },
                title = {
                    Text(text = "Anular transacciónes ")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostSate)
        },
    ) {
        LazyColumn() {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            list.let {
                items(it.size) { index ->
                    itemListCancel(item = list[index]){
                        viewModel.onCancelAuthorization(list[index])
                    }
                }
            }
        }
    }
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = false)
    BlockingCircularProgress(isLoading)

    if (!snackbar.isNullOrEmpty()){
        scope.launch {
            viewModel.clearSnackbar()
            snackbarHostSate.showSnackbar(message = snackbar)
        }
    }
}

@Composable
fun itemListCancel(item: AuthorizationEntity,onclick:()->Unit) {
    val openAlertDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(50.dp)
            .clickable {
                openAlertDialog.value = true
            },

        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(50.dp)
                .background(color = Color.White)
        ) {
            Text(text = item.id.toString())
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = item.rrn)
        }
    }

    if (openAlertDialog.value){
        AlertDialogCancel {
            openAlertDialog.value=false
            onclick.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun itemListCancelPreview() {
    val item =
        AuthorizationEntity(receiptId = "", rrn = "", statusCode = "", commerceCode = "", terminalCode = "", statusDescription = "")
    itemList(item)
}