package com.paulbrugman.app.themoviedb.models.reviews

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("author_details")
    val author_details: Author? = null,

    @SerializedName("content")
    val content: String? = null
)