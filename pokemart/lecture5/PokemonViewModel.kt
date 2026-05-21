package com.example.e_media.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_media.model.PokemonResult
import com.example.e_media.network.PokeApiService
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {
    private val apiService = PokeApiService.create()

    var pokemonList = mutableStateOf<List<PokemonResult>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    val likedPokemon = mutableStateListOf<String>()

    init {
        fetchPokemon()
    }

    private fun fetchPokemon() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = apiService.getPokemonList()
                pokemonList.value = response.results
            } catch (e: Exception) {
                // Handle error
            } finally {
                isLoading.value = false
            }
        }
    }

    fun toggleLike(name: String) {
        if (likedPokemon.contains(name)) {
            likedPokemon.remove(name)
        } else {
            likedPokemon.add(name)
        }
    }
}
