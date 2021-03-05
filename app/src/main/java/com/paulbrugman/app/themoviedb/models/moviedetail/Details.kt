package com.paulbrugman.app.themoviedb.models.moviedetail

import com.google.gson.annotations.SerializedName

data class Details(
        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("poster_path")
        val poster_path: String? = null,

        @SerializedName("release_date")
        val release_date: String? = null,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("overview")
        val overview: String? = null,

        @SerializedName("vote_average")
        val vote_average: String? = null,

        @SerializedName("videos")
        val videos: Video? = null
)