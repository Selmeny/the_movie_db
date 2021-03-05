package com.paulbrugman.app.themoviedb.models.genre

import com.google.gson.annotations.SerializedName

data class Genres(
    @SerializedName("genres")
    val genres: List<Genre>? = null
)