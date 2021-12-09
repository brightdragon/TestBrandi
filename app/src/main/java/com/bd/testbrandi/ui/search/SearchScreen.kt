package com.bd.testbrandi.ui.search

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.bd.testbrandi.ScreenRoutes
import com.bd.testbrandi.model.Documents
import com.bd.testbrandi.ui.theme.DarkTheme_Primary
import com.bd.testbrandi.ui.theme.Purple200
import com.bd.testbrandi.ui.theme.Purple500
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.delay

@Composable
private fun rememberSearchState(
    query: TextFieldValue = TextFieldValue(""),
    focused: Boolean = false,
    searching: Boolean = false
): SearchState{
    return remember {
        SearchState(
            query = query,
            focused = focused,
            searching = searching
        )
    }
}

@Stable
class SearchState(
    query: TextFieldValue,
    focused: Boolean,
    searching: Boolean
) {
    var query by mutableStateOf(query)
    var focused by mutableStateOf(focused)
    var searching by mutableStateOf(searching)
}

@ExperimentalFoundationApi
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navController: NavController,
    state: SearchState = rememberSearchState()) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                SearchTopBar(viewModel, state)
            },
            content = {
                SearchList(viewModel, navController, state)
            }
        )
    }
}

@Composable
fun SearchTopBar(viewModel: SearchViewModel, state: SearchState) {

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        elevation = 3.dp
    ) {
        SearchEdit(
            viewModel = viewModel,
            query = state.query,
            onQueryChange = { state.query = it },
            searchFocused = state.focused,
            onSearchFocusChange = { state.focused = it },
            onClearQuery = { state.query = TextFieldValue("") },
            searching = state.searching
        )
    }
}

@Composable
fun SearchEdit(viewModel: SearchViewModel,
               query: TextFieldValue,
               onQueryChange: (TextFieldValue) -> Unit,
               onSearchFocusChange: (Boolean) -> Unit,
               onClearQuery: () -> Unit,
               searchFocused: Boolean,
               searching: Boolean) {

    val focusManager = LocalFocusManager.current

    Box(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search"
            )

            Spacer(Modifier.width(5.dp))

            TextField(
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                modifier = Modifier.border(BorderStroke(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(listOf(Color.Gray, DarkTheme_Primary))),
                    shape = RoundedCornerShape(50)).fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                trailingIcon = {
                    if (query.text.isNotEmpty()) {
                        IconButton(onClick = onClearQuery ) {
                            Icon(
                                modifier = Modifier.size(width = 20.dp, height = 20.dp),
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null
                            )
                        }
                    }
                },
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                ),
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun SearchList(viewModel: SearchViewModel, navController: NavController, state: SearchState){

    val documentList = viewModel.search(state.query.text).collectAsLazyPagingItems()

    LazyVerticalGrid(
        cells = GridCells.Fixed(3),
        content = {
            items(documentList.itemCount) { index ->
                val item = documentList.getAsState(index = index)

                item?.let {
                    SearchItem(
                        viewModel = viewModel,
                        documents = it.value,
                        navController = navController
                    )
                }
            }
        }
    )
}

@Composable
fun SearchItem(viewModel: SearchViewModel, documents: Documents?, navController: NavController) {

    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .clickable { }) {

        Row{
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp),
                color = MaterialTheme.colors.surface.copy(alpha = 0.2f)
            ) {
                documents?.let { documents ->
                    val image = rememberCoilPainter(request = documents.thumbnail_url, fadeIn = true)

                    Image(
                        painter = image,
                        contentDescription = null,
                        modifier = Modifier
                            .height(120.dp)
                            .clip(shape = RoundedCornerShape(0.dp))
                            .clickable(
                                onClick = {
                                    KeyboardActions(
                                        onNext = {
                                            focusManager.moveFocus(FocusDirection.Down)
                                        }
                                    )

                                    viewModel.settingDocument(documents)
                                    navController.navigate(ScreenRoutes.DETAIL.routeName)
                                }
                            )
                        ,
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
    }
}