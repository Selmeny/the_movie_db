package com.paulbrugman.app.themoviedb.presenter

import com.paulbrugman.app.themoviedb.api.Repository
import com.paulbrugman.app.themoviedb.api.RepositoryCallback
import com.paulbrugman.app.themoviedb.interfaces.DetailCallback
import com.paulbrugman.app.themoviedb.interfaces.GenreCallback
import com.paulbrugman.app.themoviedb.interfaces.MovieCallback
import com.paulbrugman.app.themoviedb.interfaces.ReviewCallback
import com.paulbrugman.app.themoviedb.models.genre.Genres
import com.paulbrugman.app.themoviedb.models.moviedetail.Details
import com.paulbrugman.app.themoviedb.models.movies.Movies
import com.paulbrugman.app.themoviedb.models.reviews.Reviews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NetworkPresenter {
    companion object {
        fun getGenres(apiKey: String,
                      scope: CoroutineScope,
                      repository: Repository,
                      callback: GenreCallback)
        : Job {
            return scope.launch {
                repository.getGenres(apiKey, object : RepositoryCallback<Genres?> {
                    override fun onDataSuccess(data: Genres?) {
                        callback.loadGenre(data)
                    }

                    override fun onDataFailed(error: String?) {
                        callback.loadGenreFailed(error)
                    }
                })
            }
        }

        fun discoverMovie(apiKey: String,
                          page: Int,
                          id: Int,
                          scope: CoroutineScope,
                          repository: Repository,
                          callback: MovieCallback)
        : Job {
            return scope.launch {
                repository.discoverMovies(apiKey, page, id, object : RepositoryCallback<Movies?> {
                    override fun onDataSuccess(data: Movies?) {
                        callback.loadMovie(data)
                    }

                    override fun onDataFailed(error: String?) {
                        callback.loadMovieFailed(error)
                    }
                })
            }

        }

        fun getDetails(apiKey: String,
                       id: Int,
                       scope: CoroutineScope,
                       repository: Repository,
                       callback: DetailCallback)
        : Job {
            return scope.launch {
                repository.getDetails(id, apiKey, object : RepositoryCallback<Details?> {
                    override fun onDataSuccess(data: Details?) {
                        callback.loadDetail(data)
                    }

                    override fun onDataFailed(error: String?) {
                        callback.loadDetailFailed(error)
                    }
                })
            }

        }

        fun getReviews(apiKey: String,
                       page: Int,
                       id: Int,
                       scope: CoroutineScope,
                       repository: Repository,
                       callback: ReviewCallback)
        : Job {
            return scope.launch {
                repository.getReviews(id,apiKey, page, object : RepositoryCallback<Reviews?> {
                    override fun onDataSuccess(data: Reviews?) {
                        callback.loadReview(data)
                    }

                    override fun onDataFailed(error: String?) {
                        callback.loadReviewFailed(error)
                    }
                })
            }
        }
    }
}