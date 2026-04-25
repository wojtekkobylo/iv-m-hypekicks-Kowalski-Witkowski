package com.example.mamy_hypekicks_witkowski_kowalski.Models

import java.io.Serializable

data class Shoe(
    val id: String = "",
    val brand: String = "",
    val modelName: String = "",
    val releaseYear: Int = 0,
    val resellPrice: Int = 0,
    val imageUrl: String = ""
) : Serializable
