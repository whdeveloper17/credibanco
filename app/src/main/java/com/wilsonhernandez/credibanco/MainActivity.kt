package com.wilsonhernandez.credibanco

import AuthorizationScreen
import CancelScreen
import ListScreen
import SearchScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wilsonhernandez.credibanco.ui.viewmodel.AuthorizationViewModel
import com.wilsonhernandez.credibanco.ui.view.HomeScreen
import com.wilsonhernandez.credibanco.theme.CredibanCoTheme
import com.wilsonhernandez.credibanco.ui.viewmodel.CancelViewModel
import com.wilsonhernandez.credibanco.ui.viewmodel.HomeViewModel
import com.wilsonhernandez.credibanco.ui.viewmodel.ListViewModel
import com.wilsonhernandez.credibanco.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelHome: HomeViewModel by viewModels()
        val viewModelAuthorization : AuthorizationViewModel by viewModels()
        val viewModelList: ListViewModel by viewModels()
        val viewModelCancel: CancelViewModel by viewModels()
        val viewModelSearch : SearchViewModel by viewModels()
        setContent {
            val navController = rememberNavController()
            CredibanCoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                viewModelHome,
                                onClickButtonAuthorization = {navController.navigate("authorization")},
                                onClickButtonSearch = {navController.navigate("search")},
                                onClickButtonList = {navController.navigate("list")},
                                onClickButtonCancel = {navController.navigate("cancel")})
                        }
                        composable("authorization"){
                            AuthorizationScreen(viewModelAuthorization) {
                                navController.popBackStack()
                            }
                        }
                        composable("search"){
                            SearchScreen(viewModelSearch){
                                navController.popBackStack()
                            }
                        }
                        composable("list"){
                            ListScreen(viewModelList){
                                navController.popBackStack()
                            }
                        }
                        composable("cancel"){
                            CancelScreen(viewModelCancel){ navController.popBackStack()}
                        }
                    }
                }
            }
        }
    }
}