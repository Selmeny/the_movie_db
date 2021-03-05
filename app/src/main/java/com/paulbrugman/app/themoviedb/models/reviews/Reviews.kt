package com.paulbrugman.app.themoviedb.models.reviews

import com.google.gson.annotations.SerializedName

data class Reviews(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("results")
    val results: List<Review>? = null
)