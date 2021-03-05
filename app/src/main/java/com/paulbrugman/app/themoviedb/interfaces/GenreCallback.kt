package com.paulbrugman.app.themoviedb.interfaces

import com.paulbrugman.app.themoviedb.models.genre.Genres

interface GenreCallback {
    fun loadGenre(data: Genres?)
    fun loadGenreFailed(error: String?)
}