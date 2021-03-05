package com.paulbrugman.app.themoviedb.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.paulbrugman.app.themoviedb.BuildConfig
import com.paulbrugman.app.themoviedb.databinding.ItemMovieBinding
import com.paulbrugman.app.themoviedb.interfaces.MainCallback
import com.paulbrugman.app.themoviedb.models.movies.Movie
import com.paulbrugman.app.themoviedb.utilities.Constants


class MovieAdapter(private val context: Context?,
                   private val data: List<Movie>,
                   private val callback: MainCallback?): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private lateinit var movieBinding: ItemMovieBinding

    inner class MovieViewHolder(private val movieBinding: ItemMovieBinding): RecyclerView.ViewHolder(movieBinding.root) {
        fun bindData(item: Movie) {
            item.poster_path?.let {
                Glide.with(movieBinding.imgMoviePoster)
                        .load(BuildConfig.IMAGE_BASE_URL + item.poster_path)
                        .into(movieBinding.imgMoviePoster)
            }

            item.release_date?.let {
                movieBinding.tvMovieReleaseDate.text = item.release_date.substring(0,4)
            }

            item.title?.let {
                movieBinding.tvMovieTitle.text = item.title
            }

            movieBinding.cvMovie.setOnClickListener {
                callback?.addFragment(Constants.DETAIL_FRAGMENT, item.id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        movieBinding = ItemMovieBinding.inflate(LayoutInflater.from(context), parent, false)
        return MovieViewHolder(movieBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindData(data[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}