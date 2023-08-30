package com.wilsonhernandez.credibanco

import AuthorizationScreen
import CancelScreen
import ListScreen
import SearchScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.wilsonhernandez.credibanco.core.room.AuthorizationDatabase
import com.wilsonhernandez.credibanco.home.ui.HomeScreen
import com.wilsonhernandez.credibanco.ui.theme.CredibanCoTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val database =
                Room.databaseBuilder(this, AuthorizationDatabase::class.java, "authorization_db")
                    .build()
            val dao = database.dao
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
                                dao,
                                onClickButtonAuthorization = {navController.navigate("authorization")},
                                onClickButtonSearch = {navController.navigate("search")},
                                onClickButtonList = {navController.navigate("list")},
                                onClickButtonCancel = {navController.navigate("cancel")})
                        }
                        composable("authorization"){
                            AuthorizationScreen(dao) {
                                navController.popBackStack()
                            }
                        }
                        composable("search"){
                            SearchScreen()
                        }
                        composable("list"){
                            ListScreen(dao){
                                navController.popBackStack()
                            }
                        }
                        composable("cancel"){
                            CancelScreen(dao){ navController.popBackStack()}
                        }
                    }
                }
            }
        }
    }
}