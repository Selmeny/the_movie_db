package com.paulbrugman.app.themoviedb.models.moviedetail

import com.google.gson.annotations.SerializedName

data class Video (
        @SerializedName("results")
        val results: List<Key>? = null
)