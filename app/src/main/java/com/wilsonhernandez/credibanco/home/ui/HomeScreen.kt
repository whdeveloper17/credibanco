package com.wilsonhernandez.credibanco.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wilsonhernandez.credibanco.core.room.AuthorizationDao
import com.wilsonhernandez.credibanco.ui.theme.Purple40

@Composable
fun HomeScreen(
    dao: AuthorizationDao?,
    onClickButtonAuthorization: () -> Unit,
    onClickButtonSearch: () -> Unit,
    onClickButtonList: () -> Unit,
    onClickButtonCancel: () -> Unit
) {
    val viewModel = HomeViewModel(dao!!)
    val count: String by viewModel.countAuthorization.observeAsState(initial = "")
    Column(modifier = Modifier.fillMaxWidth()) {
        BankInformation(count)
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ButtonAction(title = "Autorización de transacciones", icon = Icons.Outlined.Add, onClickButtonAuthorization)
            ButtonAction(title = "Buscar transacciones", icon = Icons.Outlined.Search, {})

        }
        Spacer(modifier = Modifier.padding(bottom = 30.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ButtonAction(title = "Lista de transacciones", icon = Icons.Outlined.List,onClickButtonList)
            ButtonAction(title = "Anular transacciones", icon = Icons.Outlined.Close, onClickButtonCancel)

        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        null,
        onClickButtonAuthorization = {},
        onClickButtonSearch = {},
        onClickButtonList = {},
        onClickButtonCancel = {})
}

@Composable
fun BankInformation(count:String) {
    Card(
        shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp),
    ) {
        Column(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(color = Purple40)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "CredibanCo",
                    fontFamily = FontFamily.Serif,
                    color = Color.White,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Wilson Hernandez ", fontFamily = FontFamily.SansSerif, color = Color.White)
            Spacer(modifier = Modifier.height(10.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Confianza que Paga: Credibanco a tu Lado",
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun BankInformationPreview() {
    BankInformation("0")
}

@Composable
fun ButtonAction(title: String, icon: ImageVector, action: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .padding()
            .clickable { action.invoke() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(top = 10.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon, contentDescription = title, tint = Purple40, modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .padding(bottom = 5.dp)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = title,
                fontFamily = FontFamily.SansSerif,
                color = Purple40,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )

        }
    }
}

@Preview
@Composable
fun ButtonActionPreview() {
    ButtonAction(title = "Transacciones", icon = Icons.Outlined.Add, {})
}

@Composable
fun ListOfLastTransactions() {
    LazyColumn() {

    }
}

@Preview
@Composable
fun ListOfLastTransactionsPreview() {
    ListOfLastTransactions()
}