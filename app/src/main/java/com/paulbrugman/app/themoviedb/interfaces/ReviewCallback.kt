package com.paulbrugman.app.themoviedb.interfaces

import com.paulbrugman.app.themoviedb.models.reviews.Reviews

interface ReviewCallback {
    fun loadReview(data: Reviews?)
    fun loadReviewFailed(error: String?)
}