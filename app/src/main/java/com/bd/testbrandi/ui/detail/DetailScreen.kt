package com.bd.testbrandi.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bd.testbrandi.ui.search.SearchViewModel
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun DetailScreen(viewModel: SearchViewModel, navController: NavController){

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            topBar = {
                DetailTopBar(navController = navController)
            },
            content = {
                DetailContent(viewModel)
            }
        )
    }
}

@Composable
fun DetailTopBar(navController: NavController){

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        elevation = 3.dp
    ){
        IconButton(onClick = { navController.navigateUp() }
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "back")
        }
    }
}

@Composable
fun DetailContent(viewModel: SearchViewModel){

    val scrollState = rememberScrollState()
    val documents = viewModel.documents.value

    documents?.let {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
            ) {

                val image = rememberCoilPainter(request = documents.image_url, fadeIn = true)

                Image(
                    modifier = Modifier
                        .width(documents.width.dp)
                        .height(documents.height.dp),
                    painter = image,
                    contentScale = ContentScale.Crop,
                    contentDescription = "url")

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 5.dp,end = 5.dp)
                ) {
                    Text(
                        text = documents.display_sitename,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold)

                    Text(text = documents.datetime.toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold)
                }
            }
        }// ?: emptyView()
    }
}