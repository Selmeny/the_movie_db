package com.paulbrugman.app.themoviedb.models.genre

import com.google.gson.annotations.SerializedName

data class Genre (
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null
)