package com.paulbrugman.app.themoviedb.utilities

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paulbrugman.app.themoviedb.utilities.Constants.Companion.GRID_LAYOUT
import com.paulbrugman.app.themoviedb.utilities.Constants.Companion.LINEAR_LAYOUT
import timber.log.Timber

abstract class PaginationListener(private val type: String?,
                                  private val layoutManager: RecyclerView.LayoutManager,
                                  private val pageSize: Int) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        when (type) {
            LINEAR_LAYOUT -> {
                layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading()) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                        loadMoreItems()
                        Timber.e("loadMoreItems()")
                    }
                }
            }

            GRID_LAYOUT -> {
                layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading()) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                        loadMoreItems()
                        Timber.e("loadMoreItems()")
                    }
                }
            }
        }
    }

    protected abstract fun loadMoreItems()
    protected abstract fun isLoading(): Boolean

}