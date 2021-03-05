package com.paulbrugman.app.themoviedb.models.movies

import com.google.gson.annotations.SerializedName

data class Movies(
    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("results")
    val results: List<Movie>? = null,
)