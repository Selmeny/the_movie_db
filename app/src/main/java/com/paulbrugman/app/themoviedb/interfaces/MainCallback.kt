package com.paulbrugman.app.themoviedb.interfaces

interface MainCallback {
    fun addFragment(tag: String, data: Int? = null)
    fun backToHome()
}