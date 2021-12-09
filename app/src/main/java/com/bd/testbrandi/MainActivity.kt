package com.bd.testbrandi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bd.testbrandi.ui.detail.DetailScreen
import com.bd.testbrandi.ui.search.SearchScreen
import com.bd.testbrandi.ui.search.SearchViewModel
import com.bd.testbrandi.ui.theme.TestBrandiTheme

class MainActivity : ComponentActivity() {

    private val viewModel: SearchViewModel by viewModels()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            TestBrandiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavigationScreen(viewModel)
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun NavigationScreen(viewModel: SearchViewModel){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ScreenRoutes.SEARCH.routeName) {
        composable(ScreenRoutes.SEARCH.routeName){
            SearchScreen(viewModel = viewModel, navController = navController)
        }

        composable(ScreenRoutes.DETAIL.routeName){
            DetailScreen(viewModel = viewModel, navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TestBrandiTheme {
        //SearchScreen()
        //DetailScreen()
    }
}
