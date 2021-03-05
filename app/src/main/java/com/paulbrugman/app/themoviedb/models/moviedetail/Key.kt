package com.paulbrugman.app.themoviedb.models.moviedetail

import com.google.gson.annotations.SerializedName

data class Key(
        @SerializedName("key")
        val key: String? = null
)