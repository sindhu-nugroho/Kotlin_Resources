package com.example.e_media

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_media.model.PokemonResult
import com.example.e_media.ui.components.PokemonItem
import com.example.e_media.ui.components.ProfileHeader
import com.example.e_media.ui.theme.E_mediaTheme
import com.example.e_media.viewmodel.PokemonViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            E_mediaTheme {
                EcommerceApp()
            }
        }
    }
}

@Composable
fun EcommerceApp(viewModel: PokemonViewModel = viewModel()) {
    val pokemonList by viewModel.pokemonList
    val isLoading by viewModel.isLoading
    val likedPokemon = viewModel.likedPokemon

    EcommerceContent(
        pokemonList = pokemonList,
        isLoading = isLoading,
        likedPokemonNames = likedPokemon.toList(),
        onLikeToggle = { viewModel.toggleLike(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcommerceContent(
    pokemonList: List<PokemonResult>,
    isLoading: Boolean,
    likedPokemonNames: List<String>,
    onLikeToggle: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "POKÉMART", 
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified
                    ) 
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    BadgedBox(
                        badge = { Badge { Text("3") } },
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Reusable Profile Section
            ProfileHeader(userName = "Mr Sindhu")

            // Promotion Banner
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
                            )
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column {
                        Text("Summer Sale!", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Get 20% off on all Fire-types", color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }

            // Category Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Legendary", "Rare", "Fire", "Water", "Electric").forEach { cat ->
                    FilterChip(
                        selected = cat == "All",
                        onClick = { },
                        label = { Text(cat) },
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Text(
                    text = "New Arrivals",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontWeight = FontWeight.Bold
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(pokemonList) { pokemon ->
                        PokemonItem(
                            pokemon = pokemon,
                            isLiked = likedPokemonNames.contains(pokemon.name),
                            onLikeToggle = { onLikeToggle(pokemon.name) },
                            onAddToCart = { /* Handle cart */ }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EcommerceAppPreview() {
    E_mediaTheme {
        EcommerceContent(
            pokemonList = listOf(
                PokemonResult("Pikachu", "https://pokeapi.co/api/v2/pokemon/25/"),
                PokemonResult("Charizard", "https://pokeapi.co/api/v2/pokemon/6/"),
                PokemonResult("Bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
                PokemonResult("Squirtle", "https://pokeapi.co/api/v2/pokemon/7/")
            ),
            isLoading = false,
            likedPokemonNames = listOf("Pikachu"),
            onLikeToggle = {}
        )
    }
}
