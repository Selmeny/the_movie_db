package com.paulbrugman.app.themoviedb.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paulbrugman.app.themoviedb.databinding.ItemReviewBinding
import com.paulbrugman.app.themoviedb.models.reviews.Review

class ReviewAdapter(private val context: Context?,
                    private val data: List<Review>): RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    private lateinit var reviewBinding: ItemReviewBinding

    inner class ReviewViewHolder(private val  reviewBinding: ItemReviewBinding): RecyclerView.ViewHolder(reviewBinding.root) {
        fun bindData(item: Review) {
            val author = item.author_details?.username
            val score = item.author_details?.rating

            if (author != null && score != null) {
                reviewBinding.tvAuthor.text = "$author ($score)"
            } else if (author == null && score != null) {
                reviewBinding.tvAuthor.text = "(no username) ($score)"
            } else if (author != null && score == null) {
                reviewBinding.tvAuthor.text = "$author"
            }

            item.content?.let {
                reviewBinding.tvContent.text = it
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        reviewBinding = ItemReviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ReviewViewHolder(reviewBinding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bindData(data[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}