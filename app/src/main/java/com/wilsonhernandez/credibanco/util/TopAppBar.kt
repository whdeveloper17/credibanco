package com.wilsonhernandez.credibanco.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.wilsonhernandez.credibanco.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarUtil(title:String, iconBack:Boolean=true,actionIcon:@Composable () -> Unit, onclickButtonBack :()->Unit){
    TopAppBar(
        navigationIcon = {
           if (iconBack){
               IconButton(onClick = { onclickButtonBack.invoke() }) {
                   Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Atras")
               }
           }
        },
        actions = {
            actionIcon()
        },
        title = {
            Text(text = title)
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Purple40,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
    )
}