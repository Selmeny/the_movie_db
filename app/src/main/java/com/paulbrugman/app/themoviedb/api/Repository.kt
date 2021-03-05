package com.paulbrugman.app.themoviedb.api

import com.google.gson.Gson
import com.paulbrugman.app.themoviedb.models.genre.Genres
import com.paulbrugman.app.themoviedb.models.moviedetail.Details
import com.paulbrugman.app.themoviedb.models.movies.Movies
import com.paulbrugman.app.themoviedb.models.reviews.Reviews
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

class Repository(config: String) {
    private val client = ApiClient.getClient(config)

    fun getGenres(apiKey: String, callback: RepositoryCallback<Genres?>) {
        client
            .getGenres(apiKey)
            .enqueue(object : Callback<Genres?> {
                override fun onResponse(call: Call<Genres?>, response: Response<Genres?>) {
                    if (response.isSuccessful) {
                        callback.onDataSuccess(response.body())
                    } else {
                        try {
                            val error: Error = Gson().fromJson(response.errorBody()?.charStream(), Error::class.java)
                            callback.onDataFailed(error.message)
                        } catch (e: Exception) {
                            callback.onDataFailed(e.message)
                        }
                    }
                }

                override fun onFailure(call: Call<Genres?>, t: Throwable) {
                    callback.onDataFailed(t.message)
                }
            })
    }
    
    fun discoverMovies(apiKey: String, page: Int, id: Int, callback: RepositoryCallback<Movies?>) {
        client
            .discoverMovie(apiKey, page, id)
            .enqueue(object : Callback<Movies?> {
                override fun onResponse(call: Call<Movies?>, response: Response<Movies?>) {
                    if (response.isSuccessful) {
                        callback.onDataSuccess(response.body())
                    } else {
                        try {
                            val error: Error = Gson().fromJson(response.errorBody()?.charStream(), Error::class.java)
                            callback.onDataFailed(error.message)
                        } catch (e: Exception) {
                            callback.onDataFailed(e.message)
                        }
                    }
                }

                override fun onFailure(call: Call<Movies?>, t: Throwable) {
                    callback.onDataFailed(t.message)
                }
            })
    }

    fun getDetails(id: Int, apiKey: String, callback: RepositoryCallback<Details?>) {
        client
            .getDetails(id, apiKey)
            .enqueue(object : Callback<Details?> {
                override fun onResponse(call: Call<Details?>, response: Response<Details?>) {
                    if (response.isSuccessful) {
                        callback.onDataSuccess(response.body())
                    } else {
                        try {
                            val error: Error = Gson().fromJson(response.errorBody()?.charStream(), Error::class.java)
                            callback.onDataFailed(error.message)
                        } catch (e: Exception) {
                            callback.onDataFailed(e.message)
                        }
                    }
                }

                override fun onFailure(call: Call<Details?>, t: Throwable) {
                    callback.onDataFailed(t.message)
                }
            })
    }

    fun getReviews(id: Int, apiKey: String, page: Int, callback: RepositoryCallback<Reviews?>) {
        client
            .getReviews(id, apiKey, page)
            .enqueue(object : Callback<Reviews?> {
                override fun onResponse(call: Call<Reviews?>, response: Response<Reviews?>) {
                    if (response.isSuccessful) {
                        callback.onDataSuccess(response.body())
                    } else {
                        try {
                            val error: Error = Gson().fromJson(response.errorBody()?.charStream(), Error::class.java)
                            callback.onDataFailed(error.message)
                        } catch (e: Exception) {
                            callback.onDataFailed(e.message)
                        }
                    }
                }

                override fun onFailure(call: Call<Reviews?>, t: Throwable) {
                    callback.onDataFailed(t.message)
                }
            })
    }

}