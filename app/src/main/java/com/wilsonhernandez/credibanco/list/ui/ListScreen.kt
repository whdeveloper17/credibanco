import android.annotation.SuppressLint
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wilsonhernandez.credibanco.core.room.AuthorizationDao
import com.wilsonhernandez.credibanco.core.room.entity.AuthorizationEntity
import com.wilsonhernandez.credibanco.list.ui.ListViewModel
import com.wilsonhernandez.credibanco.ui.theme.Purple40
import com.wilsonhernandez.credibanco.ui.util.AlertDialogDetail

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(dao: AuthorizationDao?, onclickButtonBack: () -> Unit) {
    val viewModel = ListViewModel(dao!!)
    viewModel.getList()
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
                    Text(text = "Lista de transacciónes ")
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
        LazyColumn() {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            list.let {
                items(it.size) { index ->
                    itemList(item = list[index])
                }
            }
        }
    }

}

@Composable
fun itemList(item: AuthorizationEntity) {
    val openAlertDialog = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .height(50.dp)
            .clickable {
               openAlertDialog.value=true
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
        AlertDialogDetail(
            receiptId = item.receiptId,
            rrn = item.rrn,
            statusCode = item.statusCode,
            statusDescription = item.statusDescription,status = item.state){
            openAlertDialog.value=false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun itemListPreview() {
    val item =
        AuthorizationEntity(receiptId = "", rrn = "", statusCode = "", statusDescription = "", commerceCode = "", terminalCode = "")
    itemList(item)
}