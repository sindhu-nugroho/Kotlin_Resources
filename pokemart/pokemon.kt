package com.example.e_media.model

data class PokemonResponse(
    val results: List<PokemonResult>
)

data class PokemonResult(
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.trimEnd('/').split('/').last().toInt()
    
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

    val price: Double
        get() = (id * 10 + 5.99)
}

