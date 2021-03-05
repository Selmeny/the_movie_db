package com.paulbrugman.app.themoviedb.api

interface RepositoryCallback <T> {
    fun onDataSuccess(data: T)
    fun onDataFailed(error: String?)
}