package com.paulbrugman.app.themoviedb.models.reviews

import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("username")
    val username: String? = null,

    @SerializedName("rating")
    val rating: Double? = null
)