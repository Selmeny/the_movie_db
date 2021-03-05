package com.paulbrugman.app.themoviedb.interfaces

import com.paulbrugman.app.themoviedb.models.movies.Movies

interface MovieCallback {
    fun loadMovie(data: Movies?)
    fun loadMovieFailed(error: String?)
}