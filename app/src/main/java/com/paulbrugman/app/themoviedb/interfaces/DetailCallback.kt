package com.paulbrugman.app.themoviedb.interfaces

import com.paulbrugman.app.themoviedb.models.moviedetail.Details

interface DetailCallback {
    fun loadDetail(data: Details?)
    fun loadDetailFailed(error: String?)
}