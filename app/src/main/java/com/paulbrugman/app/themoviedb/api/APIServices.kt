package com.paulbrugman.app.themoviedb.api

import com.paulbrugman.app.themoviedb.models.genre.Genres
import com.paulbrugman.app.themoviedb.models.moviedetail.Details
import com.paulbrugman.app.themoviedb.models.movies.Movies
import com.paulbrugman.app.themoviedb.models.reviews.Reviews
import retrofit2.Call
import retrofit2.http.*

interface APIServices {
    @GET("genre/movie/list")
    fun getGenres(@Query("api_key") apiKey: String): Call<Genres>

    @GET("discover/movie")
    fun discoverMovie(@Query("api_key") apiKey: String,
                      @Query("page") page: Int,
                      @Query("with_genres") id: Int): Call<Movies>
    @GET("movie/{id}")
    fun getDetails(@Path("id") id: Int,
                   @Query("api_key") apiKey: String,
                   @Query("append_to_response") value: String? = "videos"): Call<Details>

    @GET("movie/{id}/reviews")
    fun getReviews(@Path("id") id: Int,
                   @Query("api_key") apiKey: String,
                   @Query("page") page: Int): Call<Reviews>
}