package com.paulbrugman.app.themoviedb.models.movies

import com.google.gson.annotations.SerializedName

data class Movie (
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("poster_path")
    val poster_path: String? = null,

    @SerializedName("release_date")
    val release_date: String? = null,

    @SerializedName("title")
    val title: String? = null,
)