package com.wilsonhernandez.credibanco

import AuthorizationScreen
import CancelScreen
import ListScreen
import SearchScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wilsonhernandez.credibanco.ui.home.HomeScreen
import com.wilsonhernandez.credibanco.ui.theme.CredibanCoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                                onClickButtonAuthorization = {navController.navigate("authorization")},
                                onClickButtonSearch = {navController.navigate("search")},
                                onClickButtonList = {navController.navigate("list")},
                                onClickButtonCancel = {navController.navigate("cancel")})
                        }
                        composable("authorization"){
                            AuthorizationScreen()
                        }
                        composable("search"){
                            SearchScreen()
                        }
                        composable("list"){
                            ListScreen()
                        }
                        composable("cancel"){
                            CancelScreen()
                        }
                    }
                }
            }
        }
    }
}