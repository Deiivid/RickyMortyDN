package com.example.rickymortydn.ui.character.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.rickymortydn.models.CharacterModel
import com.example.rickymortydn.ui.character.viewmodel.CharactersViewModel
import com.example.rickymortydn.ui.common.navigation.Routes
import com.example.rickymortydn.ui.common.states.ResourceState

@Composable
fun CharactersListScreen(
    navigationController: NavHostController,
) {
    val charactersViewModel: CharactersViewModel = hiltViewModel()
    val charactersState by charactersViewModel.charactersSearched.collectAsState()

    charactersViewModel.fetchCharacters()
    when (charactersState) {
        is ResourceState.Success<*> -> {
            val characters =
                (charactersState as ResourceState.Success<*>).data as List<CharacterModel.CharacterResult>
            CharacterItem(characters, navigationController)
        }

        else -> {}
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun CharacterItem(items: List<CharacterModel.CharacterResult>, navController: NavController) {
    val expandedState = remember { mutableStateMapOf<Int, Boolean>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items.forEachIndexed { index, character ->
            val expanded = expandedState[index] ?: false

            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFDAE1E7),
                    modifier = Modifier
                        .clickable {
                            expandedState[index] = !expanded
                            navController.navigate(Routes.HomeScreen.route)
                        }
                        .height(210.dp)
                        .padding(10.dp),
                    shadowElevation = 10.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier.wrapContentSize(),
                                contentAlignment = Alignment.Center,
                                content = {
                                    Text(
                                        text = character.status,
                                        fontSize = 12.sp,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier
                                            .padding(vertical = 4.dp, horizontal = 8.dp)
                                            .clip(RoundedCornerShape(24.dp))
                                            .background(Color(0xFFD1D5E1))
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = character.name,
                                fontSize = 24.sp,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(text = character.gender)

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = character.characterLocation.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        val modifier = if (expanded) {
                            Modifier.fillMaxSize()
                        } else {
                            Modifier.size(width = 100.dp, height = 140.dp)
                        }

                        Box(
                            modifier = modifier.clickable { expandedState[index] = !expanded },
                            contentAlignment = Alignment.Center,
                            content = {
                                Image(
                                    painter = rememberImagePainter(data = character.image),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


