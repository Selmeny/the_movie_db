package com.paulbrugman.app.themoviedb.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paulbrugman.app.themoviedb.databinding.ItemGenreBinding
import com.paulbrugman.app.themoviedb.interfaces.MainCallback
import com.paulbrugman.app.themoviedb.models.genre.Genre
import com.paulbrugman.app.themoviedb.utilities.Constants.Companion.MOVIE_FRAGMENT

class GenreAdapter(private val context: Context?,
                   private val data: List<Genre>,
                   private val callBack: MainCallback?): RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    private lateinit var genreBinding: ItemGenreBinding

    inner class GenreViewHolder(private val genreBinding: ItemGenreBinding): RecyclerView.ViewHolder(genreBinding.root) {
        fun bindData(item: Genre) {
            item.name?.let {
                genreBinding.tvGenres.text = item.name
            }

            genreBinding.cvGenres.setOnClickListener {
                callBack?.addFragment(MOVIE_FRAGMENT, item.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        genreBinding = ItemGenreBinding.inflate(LayoutInflater.from(context), parent, false)
        return GenreViewHolder(genreBinding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bindData(data[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}